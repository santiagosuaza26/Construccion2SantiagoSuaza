package app.clinic.application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.dto.billing.BillingCalculationResultDTO;
import app.clinic.application.dto.billing.BillingDTO;
import app.clinic.application.dto.billing.InvoiceDTO;
import app.clinic.application.service.BillingApplicationService;

/**
 * REST Controller for billing management operations.
 * Provides HTTP endpoints for billing calculations, invoice generation, and payment processing.
 * Accessible by administrative staff and human resources.
 */
@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingApplicationService billingApplicationService;

    public BillingController(BillingApplicationService billingApplicationService) {
        this.billingApplicationService = billingApplicationService;
    }

    /**
     * Calculates billing for a patient based on their orders and insurance policy.
     */
    @PostMapping("/calculate/{patientCedula}")
    public ResponseEntity<BillingCalculationResultDTO> calculateBilling(@PathVariable String patientCedula) {
        BillingCalculationResultDTO result = billingApplicationService.calculateBilling(patientCedula);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Generates an invoice for a patient.
     */
    @PostMapping("/invoice/{patientCedula}")
    public ResponseEntity<InvoiceDTO> generateInvoice(@PathVariable String patientCedula) {
        InvoiceDTO invoice = billingApplicationService.generateInvoice(patientCedula);
        return new ResponseEntity<>(invoice, HttpStatus.CREATED);
    }

    /**
     * Gets billing details for a patient.
     */
    @GetMapping("/details/{patientCedula}")
    public ResponseEntity<BillingDTO> getBillingDetails(@PathVariable String patientCedula) {
        return billingApplicationService.getBillingDetails(patientCedula)
                .map(details -> ResponseEntity.ok(details))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Gets billing history for a patient.
     */
    @GetMapping("/history/{patientCedula}")
    public ResponseEntity<List<InvoiceDTO>> getBillingHistory(@PathVariable String patientCedula) {
        List<InvoiceDTO> history = billingApplicationService.getBillingHistory(patientCedula);
        return ResponseEntity.ok(history);
    }

    /**
     * Gets billing statistics for a patient.
     */
    @GetMapping("/statistics/{patientCedula}")
    public ResponseEntity<BillingApplicationService.BillingStatisticsDTO> getBillingStatistics(@PathVariable String patientCedula) {
        BillingApplicationService.BillingStatisticsDTO statistics = billingApplicationService.getBillingStatistics(patientCedula);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Validates insurance policy for billing purposes.
     */
    @GetMapping("/validate-insurance/{patientCedula}")
    public ResponseEntity<Boolean> validateInsurancePolicyForBilling(@PathVariable String patientCedula) {
        boolean isValid = billingApplicationService.validateInsurancePolicyForBilling(patientCedula);
        return ResponseEntity.ok(isValid);
    }

    /**
     * Checks if patient has exceeded annual copayment limit.
     */
    @GetMapping("/copayment-limit/{patientCedula}")
    public ResponseEntity<Boolean> hasExceededAnnualCopaymentLimit(@PathVariable String patientCedula) {
        boolean exceeded = billingApplicationService.hasExceededAnnualCopaymentLimit(patientCedula);
        return ResponseEntity.ok(exceeded);
    }

    /**
     * Gets accumulated copayment amount for current year.
     */
    @GetMapping("/accumulated-copayment/{patientCedula}/{year}")
    public ResponseEntity<String> getAccumulatedCopaymentForYear(@PathVariable String patientCedula,
                                                                @PathVariable int year) {
        java.math.BigDecimal amount = billingApplicationService.getAccumulatedCopaymentForYear(patientCedula, year);
        return ResponseEntity.ok(amount.toString());
    }
}