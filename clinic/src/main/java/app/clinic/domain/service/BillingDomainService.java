package app.clinic.domain.service;

import java.math.BigDecimal;


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
 * Domain service for billing operations in the clinic management system.
 *
 * This service implements complex billing business rules including:
 * - Insurance policy validation and coverage calculation
 * - Annual copayment limit tracking and enforcement
 * - Dynamic copayment amount calculation based on accumulated payments
 * - Full payment requirements for uninsured patients
 *
 * Business Rules Implemented:
 * 1. Standard copayment amount: $50,000 COP per service
 * 2. Maximum annual copayment: $1,000,000 COP per patient
 * 3. Once limit reached, insurance covers 100% of remaining services
 * 4. Uninsured patients must pay full service cost
 * 5. Insurance coverage = Total Cost - Patient Copayment (when applicable)
 *
 * Pure domain service without external framework dependencies.
 *
 * @author Clinic Development Team
 * @version 2.0.0
 */
public class BillingDomainService {

    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;

    public BillingDomainService(BillingRepository billingRepository, PatientRepository patientRepository) {
        this.billingRepository = billingRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Calculates comprehensive billing for a patient based on service cost and insurance policy.
     *
     * This method implements the core billing algorithm that determines:
     * - Patient financial responsibility based on copayment rules
     * - Insurance coverage amount for the specific service
     * - Whether patient has reached annual copayment limit
     * - Full payment requirements for uninsured patients
     *
     * @param patientCedula The patient's identification number
     * @param totalCost The total cost of medical services/procedures
     * @return BillingCalculationResult containing detailed cost breakdown
     * @throws IllegalArgumentException if patient is not found in the system
     *
     * @see BillingCalculationResult for detailed result structure
     * @see InsurancePolicy for coverage validation logic
     */
    public BillingCalculationResult calculateBilling(PatientCedula patientCedula, TotalCost totalCost) {
        // Validate patient exists in the system
        Patient patient = patientRepository.findByCedula(patientCedula).orElse(null);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }

        // Extract patient's insurance information for coverage calculation
        InsurancePolicy insurancePolicy = patient.getInsurancePolicy();
        Year currentYear = Year.current();

        // Calculate accumulated copayment for the current year to determine remaining limit
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
     * Core billing calculation algorithm implementing complex insurance and copayment rules.
     *
     * This method implements the sophisticated billing logic that handles multiple scenarios:
     *
     * SCENARIO 1: Patient with Active Insurance
     * - Checks if patient has reached annual copayment limit ($1,000,000 COP)
     * - If limit reached: Insurance covers 100%, patient pays $0
     * - If limit not reached: Calculates remaining copayment capacity
     *
     * SCENARIO 2: Limited Copayment Capacity
     * - If remaining capacity >= standard copayment ($50,000): Patient pays standard amount
     * - If remaining capacity < standard copayment: Patient pays remaining capacity
     * - Insurance covers the difference in both cases
     *
     * SCENARIO 3: No Insurance Coverage
     * - Patient must pay full service cost
     * - Insurance coverage = $0
     * - Requires full payment flag set to true
     *
     * @param totalCost The total monetary value of medical services
     * @param insurancePolicy Patient's insurance information (may be null)
     * @param accumulatedResult Previous copayment calculations for current year
     * @return Comprehensive billing result with all financial breakdowns
     */
    private BillingCalculationResult calculateBillingWithRules(TotalCost totalCost,
                                                               InsurancePolicy insurancePolicy,
                                                               BillingCalculationResult accumulatedResult) {
        // Initialize all monetary values for calculation
        Money totalAmount = totalCost.getValue();
        Money copaymentAmount = Money.of(CopaymentAmount.standard().getValue());
        Money insuranceCoverage = Money.of(BigDecimal.ZERO);
        Money patientResponsibility = totalAmount;
        boolean requiresFullPayment = false;

        // Primary decision: Patient has valid insurance coverage
        if (insurancePolicy != null && insurancePolicy.isActive()) {

            // Check if patient has reached maximum annual copayment limit
            if (accumulatedResult.getPatientResponsibility().getAmount()
                .compareTo(MaximumCopaymentAmount.standard().getValue()) >= 0) {

                // LIMIT REACHED: Insurance covers everything, patient pays nothing
                // This implements the "100% coverage after limit" business rule
                insuranceCoverage = totalAmount;
                patientResponsibility = Money.of(BigDecimal.ZERO);

            } else {
                // LIMIT NOT REACHED: Calculate remaining copayment capacity
                Money remainingCopaymentCapacity = Money.of(MaximumCopaymentAmount.standard().getValue()
                    .subtract(accumulatedResult.getPatientResponsibility().getAmount()));

                // Secondary decision: Can patient afford full standard copayment?
                if (copaymentAmount.getAmount().compareTo(remainingCopaymentCapacity.getAmount()) <= 0) {
                    // SUFFICIENT CAPACITY: Patient pays standard copayment amount
                    // Insurance covers the rest (total - copayment)
                    insuranceCoverage = totalAmount.subtract(copaymentAmount);
                    patientResponsibility = copaymentAmount;

                } else {
                    // LIMITED CAPACITY: Patient pays only what they can afford
                    // Insurance covers everything beyond patient's remaining capacity
                    insuranceCoverage = totalAmount.subtract(remainingCopaymentCapacity);
                    patientResponsibility = remainingCopaymentCapacity;
                }
            }

        } else {
            // NO INSURANCE: Patient must pay full amount
            // This handles uninsured patients and expired insurance policies
            requiresFullPayment = true;
        }

        // Return comprehensive calculation result with all financial details
        return BillingCalculationResult.of(totalAmount, copaymentAmount, insuranceCoverage,
                                          patientResponsibility, requiresFullPayment);
    }
}