package app.clinic.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.GenerateBillingFromOrderUseCase;
import app.clinic.application.usecase.GenerateBillingUseCase;
import app.clinic.infrastructure.dto.BillingDTO;

@RestController
@RequestMapping("/api/billing")
public class BillingController {
    private final GenerateBillingUseCase generateBillingUseCase;
    private final GenerateBillingFromOrderUseCase generateBillingFromOrderUseCase;

    public BillingController(GenerateBillingUseCase generateBillingUseCase,
                           GenerateBillingFromOrderUseCase generateBillingFromOrderUseCase) {
        this.generateBillingUseCase = generateBillingUseCase;
        this.generateBillingFromOrderUseCase = generateBillingFromOrderUseCase;
    }

    @PostMapping
    public ResponseEntity<BillingDTO> generateBilling(@RequestBody GenerateBillingRequest request) {
        var billing = generateBillingUseCase.execute(
            request.patientId, request.doctorName, request.orderNumber, request.totalCost,
            request.appliedMedications, request.appliedProcedures, request.appliedDiagnosticAids
        );

        var dto = new BillingDTO(
            billing.getOrderNumber().getValue(), // Use order number as billing ID
            billing.getIdentificationNumber(), // Using identification number as patient ID
            billing.getPatientName(),
            billing.getDoctorName(),
            billing.getOrderNumber().getValue(),
            billing.getTotalCost(),
            billing.getCopay(), // Using copay as copayAmount
            billing.getInsuranceCoverage(),
            billing.getTotalCost() - billing.getCopay() - billing.getInsuranceCoverage(), // Calculate final amount
            billing.getAppliedMedications(),
            billing.getAppliedProcedures(),
            billing.getAppliedDiagnosticAids(),
            billing.getGeneratedAt(),
            billing.getGeneratedBy()
        );

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/from-order/{orderNumber}")
    public ResponseEntity<BillingDTO> generateBillingFromOrder(@PathVariable String orderNumber,
                                                             @RequestParam String adminId) {
        var billing = generateBillingFromOrderUseCase.execute(orderNumber, adminId);

        var dto = new BillingDTO(
            billing.getOrderNumber().getValue(), // Use order number as billing ID
            billing.getIdentificationNumber(), // Using identification number as patient ID
            billing.getPatientName(),
            billing.getDoctorName(),
            billing.getOrderNumber().getValue(),
            billing.getTotalCost(),
            billing.getCopay(), // Using copay as copayAmount
            billing.getInsuranceCoverage(),
            billing.getTotalCost() - billing.getCopay() - billing.getInsuranceCoverage(), // Calculate final amount
            billing.getAppliedMedications(),
            billing.getAppliedProcedures(),
            billing.getAppliedDiagnosticAids(),
            billing.getGeneratedAt(),
            billing.getGeneratedBy()
        );

        return ResponseEntity.ok(dto);
    }

    public static class GenerateBillingRequest {
        public String patientId;
        public String doctorName;
        public String orderNumber;
        public double totalCost;
        public String appliedMedications;
        public String appliedProcedures;
        public String appliedDiagnosticAids;
    }
}