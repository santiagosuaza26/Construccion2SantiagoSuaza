package app.clinic.domain.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.BillingCalculationResult;
import app.clinic.domain.model.BillingDetails;
import app.clinic.domain.model.BillingSummary;
import app.clinic.domain.model.CopaymentAmount;
import app.clinic.domain.model.InsurancePolicy;
import app.clinic.domain.model.MaximumCopaymentAmount;
import app.clinic.domain.model.Money;
import app.clinic.domain.model.Patient;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.TotalCost;
import app.clinic.domain.model.Year;
import app.clinic.domain.port.BillingRepository;
import app.clinic.domain.port.PatientRepository;

/**
 * Domain service for billing operations.
 * Contains business logic for billing calculations and invoice generation.
 */
@Service
public class BillingDomainService {

    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;

    public BillingDomainService(BillingRepository billingRepository, PatientRepository patientRepository) {
        this.billingRepository = billingRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Calculates billing for a patient based on their orders and insurance policy.
     */
    public BillingCalculationResult calculateBilling(PatientCedula patientCedula, TotalCost totalCost) {
        Patient patient = patientRepository.findByCedula(patientCedula).orElse(null);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }

        InsurancePolicy insurancePolicy = patient.getInsurancePolicy();
        Year currentYear = Year.current();

        // Calculate accumulated copayment for the current year
        BillingCalculationResult accumulatedResult = billingRepository.calculateAccumulatedCopayment(patientCedula, currentYear);

        return calculateBillingWithRules(totalCost, insurancePolicy, accumulatedResult);
    }

    /**
     * Generates billing summary for a patient.
     */
    public BillingSummary generateBillingSummary(PatientCedula patientCedula) {
        return billingRepository.generateBillingSummary(patientCedula);
    }

    /**
     * Generates billing details for invoice generation.
     */
    public BillingDetails generateBillingDetails(PatientCedula patientCedula) {
        return billingRepository.generateBillingDetails(patientCedula);
    }

    /**
     * Calculates billing according to business rules.
     */
    private BillingCalculationResult calculateBillingWithRules(TotalCost totalCost,
                                                              InsurancePolicy insurancePolicy,
                                                              BillingCalculationResult accumulatedResult) {
        Money totalAmount = totalCost.getValue();
        Money copaymentAmount = Money.of(CopaymentAmount.standard().getValue());
        Money insuranceCoverage = Money.of(BigDecimal.ZERO);
        Money patientResponsibility = totalAmount;
        boolean requiresFullPayment = false;

        if (insurancePolicy != null && insurancePolicy.isActive()) {
            // Patient has active insurance
            if (accumulatedResult.getPatientResponsibility().getAmount()
                .compareTo(MaximumCopaymentAmount.standard().getValue()) >= 0) {
                // Patient has reached maximum copayment for the year
                insuranceCoverage = totalAmount;
                patientResponsibility = Money.of(BigDecimal.ZERO);
            } else {
                // Calculate remaining copayment capacity
                Money remainingCopaymentCapacity = Money.of(MaximumCopaymentAmount.standard().getValue()
                    .subtract(accumulatedResult.getPatientResponsibility().getAmount()));

                if (copaymentAmount.getAmount().compareTo(remainingCopaymentCapacity.getAmount()) <= 0) {
                    // Patient can pay full copayment
                    insuranceCoverage = totalAmount.subtract(copaymentAmount);
                    patientResponsibility = copaymentAmount;
                } else {
                    // Patient has limited copayment capacity
                    insuranceCoverage = totalAmount.subtract(remainingCopaymentCapacity);
                    patientResponsibility = remainingCopaymentCapacity;
                }
            }
        } else {
            // Patient has no insurance or inactive insurance
            requiresFullPayment = true;
        }

        return BillingCalculationResult.of(totalAmount, copaymentAmount, insuranceCoverage,
                                          patientResponsibility, requiresFullPayment);
    }
}