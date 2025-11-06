package app.clinic.infrastructure.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.GenerateBillingFromOrderUseCase;
import app.clinic.domain.model.entities.Billing;
import app.clinic.domain.service.BillingService;
import app.clinic.infrastructure.dto.BillingDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/billing")
@Tag(name = "Billing", description = "API para gestión de facturación y facturas")
public class BillingController {

    private final GenerateBillingFromOrderUseCase generateBillingFromOrderUseCase;
    private final BillingService billingService;

    public BillingController(GenerateBillingFromOrderUseCase generateBillingFromOrderUseCase,
                           BillingService billingService) {
        this.generateBillingFromOrderUseCase = generateBillingFromOrderUseCase;
        this.billingService = billingService;
    }

    @GetMapping("/order/{orderNumber}/print")
    @PreAuthorize("hasAnyRole('PERSONAL_ADMINISTRATIVO', 'MEDICO')")
    @Operation(summary = "Imprimir factura", description = "Genera y descarga una factura en formato de texto para una orden específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura generada exitosamente",
            content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "400", description = "Error al generar la factura", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<byte[]> printInvoice(@PathVariable String orderNumber) {
        try {
            // Generate billing from order
            Billing billing = generateBillingFromOrderUseCase.execute(orderNumber, "system");

            // Generate printable invoice
            String invoiceText = billingService.generatePrintableInvoice(billing);

            // Convert to bytes
            byte[] invoiceBytes = invoiceText.getBytes();

            // Set headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("attachment", "factura_" + orderNumber + ".txt");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(invoiceBytes);
        } catch (Exception e) {
            // Return error response if billing generation fails
            return ResponseEntity.badRequest()
                    .body(("Error generando factura: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/order/{orderNumber}")
    @PreAuthorize("hasAnyRole('PERSONAL_ADMINISTRATIVO', 'MEDICO')")
    @Operation(summary = "Obtener facturación", description = "Obtiene la información de facturación para una orden específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facturación obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillingDTO.class))),
        @ApiResponse(responseCode = "400", description = "Error al obtener la facturación", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<BillingDTO> getBilling(@PathVariable String orderNumber) {
        try {
            Billing billing = generateBillingFromOrderUseCase.execute(orderNumber, "system");

            BillingDTO dto = new BillingDTO();
            dto.setId(billing.getOrderNumber().getValue());
            dto.setPatientId(billing.getIdentificationNumber());
            dto.setPatientName(billing.getPatientName());
            dto.setDoctorName(billing.getDoctorName());
            dto.setOrderNumber(billing.getOrderNumber().getValue());
            dto.setTotalCost(billing.getTotalCost());
            dto.setCopayAmount(billing.getCopay());
            dto.setInsuranceCoverage(billing.getInsuranceCoverage());
            dto.setFinalAmount(billing.getCopay()); // Patient pays copay
            dto.setAppliedMedications(billing.getAppliedMedications());
            dto.setAppliedProcedures(billing.getAppliedProcedures());
            dto.setAppliedDiagnosticAids(billing.getAppliedDiagnosticAids());
            dto.setGeneratedAt(billing.getGeneratedAt());
            dto.setGeneratedBy(billing.getGeneratedBy());

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{patientId}")
    @PreAuthorize("hasRole('PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Generar detalles de facturación", description = "Genera los detalles de facturación para un paciente específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalles de facturación generados exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillingDTO.class))),
        @ApiResponse(responseCode = "400", description = "Error al generar detalles de facturación", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<BillingDTO> getPatientBilling(@PathVariable String patientId) {
        try {
            // This would need a use case to generate billing for a patient
            // For now, return a placeholder response
            BillingDTO dto = new BillingDTO();
            dto.setPatientId(patientId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Crear nueva factura", description = "Crea una nueva factura con la información proporcionada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillingDTO.class))),
        @ApiResponse(responseCode = "400", description = "Error al crear la factura", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<BillingDTO> createInvoice(@RequestBody CreateInvoiceRequest request) {
        try {
            // This would need a use case to create a new invoice
            // For now, return a placeholder response
            BillingDTO dto = new BillingDTO();
            dto.setPatientId(request.patientId);
            dto.setDoctorName(request.doctorName);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/invoices")
    @PreAuthorize("hasRole('PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Listar todas las facturas", description = "Obtiene una lista de todas las facturas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de facturas obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BillingDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<java.util.List<BillingDTO>> listInvoices() {
        try {
            // This would need a use case to list all invoices
            // For now, return an empty list
            return ResponseEntity.ok(new java.util.ArrayList<>());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public static class CreateInvoiceRequest {
        public String patientId;
        public String doctorName;
        public String insuranceName;
        public String policyNumber;
        public String patientData;
        public String servicesRendered;
    }
}