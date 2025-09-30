package app.infrastructure.adapter.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.application.dto.request.GenerateInvoiceRequest;
import app.application.dto.response.CommonResponse;
import app.application.dto.response.InvoiceResponse;
import app.application.service.BillingApplicationService;
import app.domain.model.Role;
import app.domain.services.AuthenticationService.AuthenticatedUser;
import jakarta.validation.Valid;

/**
 * Controlador REST para funcionalidades de facturación
 *
 * Endpoints para personal administrativo:
 * - POST /api/billing/invoices: Generar nueva factura
 * - GET /api/billing/invoices/{invoiceId}: Obtener factura por ID
 * - GET /api/billing/invoices/patient/{patientIdCard}: Obtener facturas por paciente
 * - GET /api/billing/invoices/order/{orderNumber}: Obtener factura por orden
 * - GET /api/billing/reports/patient/{patientIdCard}: Reporte de facturación por paciente
 * - GET /api/billing/reports/period: Reporte de facturación por período
 */
@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class BillingController {

    private final BillingApplicationService billingApplicationService;

    public BillingController(BillingApplicationService billingApplicationService) {
        this.billingApplicationService = billingApplicationService;
    }

    /**
     * Generar nueva factura
     * Solo accesible para personal administrativo
     */
    @PostMapping("/invoices")
    public ResponseEntity<CommonResponse<InvoiceResponse>> generateInvoice(
            @Valid @RequestBody GenerateInvoiceRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "ADMINISTRATIVE");

            CommonResponse<InvoiceResponse> response = billingApplicationService.generateInvoice(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<InvoiceResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al generar factura", "BILLING_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtener factura por ID
     * Accesible para personal administrativo y el paciente relacionado
     */
    @GetMapping("/invoices/{invoiceId}")
    public ResponseEntity<CommonResponse<InvoiceResponse>> getInvoiceById(
            @PathVariable String invoiceId,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (invoiceId == null || invoiceId.trim().isEmpty()) {
                CommonResponse<InvoiceResponse> errorResponse = CommonResponse.error(
                    "ID de factura es requerido", "INVOICE_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "ADMINISTRATIVE");

            CommonResponse<InvoiceResponse> response = billingApplicationService.getInvoiceById(invoiceId.trim(), currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<InvoiceResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener factura", "GET_INVOICE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtener facturas por paciente
     * Accesible para personal administrativo y el propio paciente
     */
    @GetMapping("/invoices/patient/{patientIdCard}")
    public ResponseEntity<CommonResponse<List<InvoiceResponse>>> getInvoicesByPatient(
            @PathVariable String patientIdCard,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (patientIdCard == null || patientIdCard.trim().isEmpty()) {
                CommonResponse<List<InvoiceResponse>> errorResponse = CommonResponse.error(
                    "Cédula del paciente es requerida", "PATIENT_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "ADMINISTRATIVE");

            CommonResponse<List<InvoiceResponse>> response = billingApplicationService.getInvoicesByPatient(
                patientIdCard.trim(), currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<List<InvoiceResponse>> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener facturas del paciente", "GET_PATIENT_INVOICES_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtener factura por número de orden
     * Accesible para personal administrativo
     */
    @GetMapping("/invoices/order/{orderNumber}")
    public ResponseEntity<CommonResponse<InvoiceResponse>> getInvoiceByOrderNumber(
            @PathVariable String orderNumber,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (orderNumber == null || orderNumber.trim().isEmpty()) {
                CommonResponse<InvoiceResponse> errorResponse = CommonResponse.error(
                    "Número de orden es requerido", "ORDER_NUMBER_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "ADMINISTRATIVE");

            CommonResponse<InvoiceResponse> response = billingApplicationService.getInvoiceByOrderNumber(
                orderNumber.trim(), currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<InvoiceResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener factura por orden", "GET_INVOICE_BY_ORDER_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Reporte de facturación por paciente
     * Incluye totales, copagos, cobertura de seguros
     */
    @GetMapping("/reports/patient/{patientIdCard}")
    public ResponseEntity<CommonResponse<Object>> getPatientBillingReport(
            @PathVariable String patientIdCard,
            @RequestParam(required = false) Integer year,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (patientIdCard == null || patientIdCard.trim().isEmpty()) {
                CommonResponse<Object> errorResponse = CommonResponse.error(
                    "Cédula del paciente es requerida", "PATIENT_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "ADMINISTRATIVE");

            int reportYear = year != null ? year : java.time.LocalDate.now().getYear();

            CommonResponse<Object> response = billingApplicationService.getPatientBillingReport(
                patientIdCard.trim(), reportYear, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<Object> errorResponse = CommonResponse.error(
                "Error interno del servidor al generar reporte de facturación", "BILLING_REPORT_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Reporte de facturación por período
     * Para análisis administrativo y financiero
     */
    @GetMapping("/reports/period")
    public ResponseEntity<CommonResponse<Object>> getPeriodBillingReport(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (startDate == null || startDate.trim().isEmpty()) {
                CommonResponse<Object> errorResponse = CommonResponse.error(
                    "Fecha de inicio es requerida", "START_DATE_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            if (endDate == null || endDate.trim().isEmpty()) {
                CommonResponse<Object> errorResponse = CommonResponse.error(
                    "Fecha de fin es requerida", "END_DATE_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "ADMINISTRATIVE");

            CommonResponse<Object> response = billingApplicationService.getPeriodBillingReport(
                startDate.trim(), endDate.trim(), currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<Object> errorResponse = CommonResponse.error(
                "Error interno del servidor al generar reporte por período", "PERIOD_REPORT_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Calcular copago para una orden específica
     * Utilidad para preview antes de generar factura
     */
    @GetMapping("/calculate-copay")
    public ResponseEntity<CommonResponse<Object>> calculateCopay(
            @RequestParam String patientIdCard,
            @RequestParam String orderNumber,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (patientIdCard == null || patientIdCard.trim().isEmpty()) {
                CommonResponse<Object> errorResponse = CommonResponse.error(
                    "Cédula del paciente es requerida", "PATIENT_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            if (orderNumber == null || orderNumber.trim().isEmpty()) {
                CommonResponse<Object> errorResponse = CommonResponse.error(
                    "Número de orden es requerido", "ORDER_NUMBER_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "ADMINISTRATIVE");

            CommonResponse<Object> response = billingApplicationService.calculateCopayPreview(
                patientIdCard.trim(), orderNumber.trim(), currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<Object> errorResponse = CommonResponse.error(
                "Error interno del servidor al calcular copago", "COPAY_CALCULATION_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Método temporal para crear usuario autenticado mock
     * TODO: Reemplazar con implementación real de JWT
     */
    private AuthenticatedUser createMockAuthenticatedUser(String userId, String roleString) {
        if (userId == null) {
            return new AuthenticatedUser("12345678", "Mock User", Role.valueOf(roleString), true);
        }
        return new AuthenticatedUser(userId, "Mock User", Role.valueOf(roleString), true);
    }

    /**
     * Endpoint de prueba simple para verificar que el controlador funciona
     */
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Billing controller is working!");
    }
}