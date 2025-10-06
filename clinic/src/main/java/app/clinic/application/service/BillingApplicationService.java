package app.clinic.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import app.clinic.application.dto.billing.BillingCalculationResultDTO;
import app.clinic.application.dto.billing.BillingDTO;
import app.clinic.application.dto.billing.InvoiceDTO;
import app.clinic.application.dto.billing.OrderSummaryDTO;
import app.clinic.application.dto.patient.PatientDTO;
import app.clinic.application.mapper.BillingMapper;
import app.clinic.domain.service.BillingDomainService;
import app.clinic.infrastructure.adapter.BillingRepositoryAdapter;
import app.clinic.infrastructure.entity.InvoiceEntity;
import app.clinic.infrastructure.repository.InvoiceJpaRepository;

/**
 * Application service for billing management operations.
 * Coordinates between REST controllers and domain services.
 * Handles billing-related use cases and business operations including copayment calculations.
 */
@Service
public class BillingApplicationService {

    private final BillingDomainService billingDomainService;
    private final PatientApplicationService patientApplicationService;
    private final BillingRepositoryAdapter billingRepositoryAdapter;
    private final BillingMapper billingMapper;
    private final InvoiceJpaRepository invoiceJpaRepository;

    // Constants for copayment rules
    private static final BigDecimal COPAYMENT_AMOUNT = new BigDecimal("50000"); // $50,000
    private static final BigDecimal ANNUAL_COPAYMENT_LIMIT = new BigDecimal("1000000"); // $1,000,000

    public BillingApplicationService(BillingDomainService billingDomainService,
                                    PatientApplicationService patientApplicationService,
                                    BillingRepositoryAdapter billingRepositoryAdapter,
                                    BillingMapper billingMapper,
                                    InvoiceJpaRepository invoiceJpaRepository) {
        this.billingDomainService = billingDomainService;
        this.patientApplicationService = patientApplicationService;
        this.billingRepositoryAdapter = billingRepositoryAdapter;
        this.billingMapper = billingMapper;
        this.invoiceJpaRepository = invoiceJpaRepository;
    }

    /**
     * Calculates billing for a patient based on their orders and insurance policy.
     */
    public BillingCalculationResultDTO calculateBilling(String patientCedula) {
        java.math.BigDecimal totalAmount = java.math.BigDecimal.valueOf(200000); // $200,000 placeholder

        // Check if patient has active insurance
        boolean hasActiveInsurance = validateInsurancePolicyForBilling(patientCedula);

        // Apply business rules
        return applyBillingBusinessRules(patientCedula, totalAmount, hasActiveInsurance);
    }

    /**
     * Generates an invoice for a patient.
     */
    public InvoiceDTO generateInvoice(String patientCedula) {
        // Calculate billing using existing method
        BillingCalculationResultDTO calculationResult = calculateBilling(patientCedula);

        // Generate unique invoice number
        String invoiceNumber = generateUniqueInvoiceNumber();

        // Get patient information
        String patientName = patientApplicationService.findPatientByCedula(patientCedula)
                .map(patient -> patient.getFullName())
                .orElse("Paciente no encontrado");

        // Create and save invoice entity through domain service
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setInvoiceNumber(invoiceNumber);
        invoiceEntity.setPatientCedula(patientCedula);
        invoiceEntity.setTotalAmount(new BigDecimal(calculationResult.getTotalCost()));
        invoiceEntity.setCopaymentAmount(new BigDecimal(calculationResult.getCopaymentAmount()));
        invoiceEntity.setInsuranceCoverage(new BigDecimal(calculationResult.getInsuranceCoverageAmount()));
        invoiceEntity.setPatientResponsibility(new BigDecimal(calculationResult.getCopaymentAmount()));

        // Set dates
        LocalDateTime now = LocalDateTime.now();
        invoiceEntity.setBillingDate(now);
        invoiceEntity.setDueDate(now.plusDays(30));
        invoiceEntity.setStatus(InvoiceEntity.InvoiceStatus.PENDING);
        invoiceEntity.setYear(Year.now().getValue());
        invoiceEntity.setNotes("Factura generada automáticamente");

        // Save invoice using repository adapter
        InvoiceEntity savedInvoice = invoiceJpaRepository.save(invoiceEntity);

        // Convert to DTO using mapper
        return billingMapper.toInvoiceDTO(savedInvoice);
    }

    /**
     * Gets billing details for a patient.
     */
    public Optional<BillingDTO> getBillingDetails(String patientCedula) {
        // Get patient information
        Optional<PatientDTO> patientOpt = patientApplicationService.findPatientByCedula(patientCedula);
        if (patientOpt.isEmpty()) {
            return Optional.empty();
        }

        PatientDTO patient = patientOpt.get();

        // Get patient invoices for order summaries
        List<InvoiceEntity> invoices = invoiceJpaRepository.findByPatientCedulaOrderByBillingDateDesc(patientCedula);

        // Create order summaries from invoices (simplified implementation)
        List<OrderSummaryDTO> orderSummaries = invoices.stream()
            .map(invoice -> new OrderSummaryDTO(
                invoice.getInvoiceNumber(),
                List.of(), // medications - would need separate query
                List.of(), // procedures - would need separate query
                List.of()  // diagnostic aids - would need separate query
            ))
            .toList();

        // Calculate totals from invoices
        BigDecimal totalAmount = invoices.stream()
            .map(InvoiceEntity::getTotalAmount)
            .filter(amount -> amount != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCopayment = invoices.stream()
            .map(InvoiceEntity::getCopaymentAmount)
            .filter(amount -> amount != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalInsuranceCoverage = invoices.stream()
            .map(InvoiceEntity::getInsuranceCoverage)
            .filter(amount -> amount != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Get insurance information
        String insuranceCompany = "";
        String policyNumber = "";
        Integer validityDays = null;
        String expirationDate = "";

        if (patient.getInsurancePolicy() != null) {
            insuranceCompany = patient.getInsurancePolicy().getCompanyName();
            policyNumber = patient.getInsurancePolicy().getPolicyNumber();
            if (patient.getInsurancePolicy().getExpirationDate() != null) {
                expirationDate = patient.getInsurancePolicy().getExpirationDate().toString();
                // Calculate validity days (simplified calculation)
                validityDays = 365; // Default 1 year validity
            }
        }

        // Create and return billing DTO
        BillingDTO billingDTO = new BillingDTO(
            patient.getFullName(),
            patient.getAge(),
            patientCedula,
            insuranceCompany,
            policyNumber,
            validityDays,
            expirationDate,
            orderSummaries,
            totalAmount.toString(),
            totalCopayment.toString(),
            totalInsuranceCoverage.toString()
        );

        return Optional.of(billingDTO);
    }

    /**
     * Calculates copayment amount based on insurance policy and annual limit.
     */
    public BigDecimal calculateCopaymentAmount(String patientCedula, BigDecimal totalAmount) {
        // Check if patient has active insurance
        boolean hasActiveInsurance = validateInsurancePolicyForBilling(patientCedula);

        if (!hasActiveInsurance) {
            // No insurance or inactive insurance - patient pays everything
            return totalAmount;
        }

        // Check if patient has exceeded annual copayment limit
        if (hasExceededAnnualCopaymentLimit(patientCedula)) {
            // Annual limit exceeded - insurance covers everything, no copayment
            return BigDecimal.ZERO;
        }

        // Apply standard copayment of $50,000
        // Ensure copayment doesn't exceed total amount
        return COPAYMENT_AMOUNT.min(totalAmount);
    }

    /**
     * Checks if patient has exceeded annual copayment limit.
     */
    public boolean hasExceededAnnualCopaymentLimit(String patientCedula) {
        // Get accumulated copayment for current year
        BigDecimal accumulatedCopayment = getAccumulatedCopaymentForYear(patientCedula, Year.now().getValue());

        // Check if accumulated copayment exceeds annual limit
        return accumulatedCopayment.compareTo(ANNUAL_COPAYMENT_LIMIT) >= 0;
    }

    /**
     * Gets accumulated copayment amount for current year.
     */
    public BigDecimal getAccumulatedCopaymentForYear(String patientCedula, int year) {
        // Use the repository method to get total copayment for the year
        BigDecimal totalCopayment = invoiceJpaRepository.getTotalCopaymentForYear(patientCedula, year);
        return totalCopayment != null ? totalCopayment : BigDecimal.ZERO;
    }

    /**
     * Validates insurance policy for billing purposes.
     */
    public boolean validateInsurancePolicyForBilling(String patientCedula) {
        // Get patient information
        Optional<PatientDTO> patientOpt = patientApplicationService.findPatientByCedula(patientCedula);

        if (patientOpt.isEmpty()) {
            return false;
        }

        PatientDTO patient = patientOpt.get();

        // Check if patient has insurance policy
        if (patient.getInsurancePolicy() == null) {
            return false;
        }

        // Check if insurance policy is active
        return patient.getInsurancePolicy().isActive();
    }

    /**
     * Applies business rules for billing calculation.
     */
    public BillingCalculationResultDTO applyBillingBusinessRules(String patientCedula,
                                                                 BigDecimal totalAmount,
                                                                 boolean hasActiveInsurance) {
        BillingCalculationResultDTO result = new BillingCalculationResultDTO();

        if (hasActiveInsurance) {
            // Patient has active insurance
            if (hasExceededAnnualCopaymentLimit(patientCedula)) {
                // Annual limit exceeded - insurance covers everything
                result.setTotalCost(totalAmount.toString());
                result.setCopaymentAmount("0");
                result.setInsuranceCoverageAmount(totalAmount.toString());
                result.setRequiresCopayment(false);
                result.setCopaymentLimitExceeded(true);
                result.setCopaymentLimitMessage("Paciente ha excedido el límite anual de copagos ($1,000,000). La aseguradora cubrirá el total.");
            } else {
                // Apply standard copayment of $50,000
                BigDecimal copayment = COPAYMENT_AMOUNT;
                BigDecimal insuranceCoverage = totalAmount.subtract(copayment);

                result.setTotalCost(totalAmount.toString());
                result.setCopaymentAmount(copayment.toString());
                result.setInsuranceCoverageAmount(insuranceCoverage.toString());
                result.setRequiresCopayment(true);
                result.setCopaymentLimitExceeded(false);
                result.setCopaymentLimitMessage("Paciente debe pagar copago de $50,000. El resto lo cubre la aseguradora.");
            }
        } else {
            // No insurance or inactive insurance
            result.setTotalCost(totalAmount.toString());
            result.setCopaymentAmount(totalAmount.toString());
            result.setInsuranceCoverageAmount("0");
            result.setRequiresCopayment(true);
            result.setCopaymentLimitExceeded(false);
            result.setCopaymentLimitMessage("Paciente debe pagar el total de los servicios. Sin póliza activa o póliza expirada.");
        }

        return result;
    }

    /**
     * Gets billing history for a patient.
     */
    public List<InvoiceDTO> getBillingHistory(String patientCedula) {
        // Get all invoices for the patient ordered by billing date (most recent first)
        List<InvoiceEntity> invoices = invoiceJpaRepository.findByPatientCedulaOrderByBillingDateDesc(patientCedula);

        // Convert entities to DTOs using the billing mapper
        return billingMapper.toInvoiceDTOList(invoices);
    }

    /**
     * Gets billing statistics for a patient.
     */
    public BillingStatisticsDTO getBillingStatistics(String patientCedula) {
        // Get all invoices for the patient
        List<InvoiceEntity> invoices = invoiceJpaRepository.findByPatientCedulaOrderByBillingDateDesc(patientCedula);

        // Filter out cancelled invoices for statistics
        List<InvoiceEntity> validInvoices = invoices.stream()
            .filter(invoice -> !InvoiceEntity.InvoiceStatus.CANCELLED.equals(invoice.getStatus()))
            .toList();

        if (validInvoices.isEmpty()) {
            return new BillingStatisticsDTO(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                0,
                "0"
            );
        }

        // Calculate totals
        BigDecimal totalBilled = validInvoices.stream()
            .map(InvoiceEntity::getTotalAmount)
            .filter(amount -> amount != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPaidByPatient = validInvoices.stream()
            .map(InvoiceEntity::getCopaymentAmount)
            .filter(amount -> amount != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPaidByInsurance = validInvoices.stream()
            .map(InvoiceEntity::getInsuranceCoverage)
            .filter(amount -> amount != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate average billing amount
        String averageBillingAmount = totalBilled.divide(
            BigDecimal.valueOf(validInvoices.size()),
            2,
            java.math.RoundingMode.HALF_UP
        ).toString();

        return new BillingStatisticsDTO(
            totalBilled,
            totalPaidByPatient,
            totalPaidByInsurance,
            validInvoices.size(),
            averageBillingAmount
        );
    }

    /**
     * DTO for billing statistics.
     */
    public static class BillingStatisticsDTO {
        private BigDecimal totalBilled;
        private BigDecimal totalPaidByPatient;
        private BigDecimal totalPaidByInsurance;
        private int numberOfInvoices;
        private String averageBillingAmount;

        // Constructors, getters and setters
        public BillingStatisticsDTO() {}

        public BillingStatisticsDTO(BigDecimal totalBilled, BigDecimal totalPaidByPatient,
                                   BigDecimal totalPaidByInsurance, int numberOfInvoices, String averageBillingAmount) {
            this.totalBilled = totalBilled;
            this.totalPaidByPatient = totalPaidByPatient;
            this.totalPaidByInsurance = totalPaidByInsurance;
            this.numberOfInvoices = numberOfInvoices;
            this.averageBillingAmount = averageBillingAmount;
        }

        // Getters and setters
        public BigDecimal getTotalBilled() { return totalBilled; }
        public void setTotalBilled(BigDecimal totalBilled) { this.totalBilled = totalBilled; }
        public BigDecimal getTotalPaidByPatient() { return totalPaidByPatient; }
        public void setTotalPaidByPatient(BigDecimal totalPaidByPatient) { this.totalPaidByPatient = totalPaidByPatient; }
        public BigDecimal getTotalPaidByInsurance() { return totalPaidByInsurance; }
        public void setTotalPaidByInsurance(BigDecimal totalPaidByInsurance) { this.totalPaidByInsurance = totalPaidByInsurance; }
        public int getNumberOfInvoices() { return numberOfInvoices; }
        public void setNumberOfInvoices(int numberOfInvoices) { this.numberOfInvoices = numberOfInvoices; }
        public String getAverageBillingAmount() { return averageBillingAmount; }
        public void setAverageBillingAmount(String averageBillingAmount) { this.averageBillingAmount = averageBillingAmount; }
    }

    /**
     * Generates a unique invoice number.
     */
    private String generateUniqueInvoiceNumber() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomSuffix = String.valueOf((int) (Math.random() * 1000));
        return "INV-" + timestamp + "-" + randomSuffix;
    }
}