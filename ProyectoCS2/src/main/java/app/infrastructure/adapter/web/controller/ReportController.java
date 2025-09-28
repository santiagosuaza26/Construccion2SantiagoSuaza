package app.infrastructure.adapter.web.controller;

import app.application.dto.response.CommonResponse;
import app.application.service.ReportApplicationService;
import app.application.service.ReportApplicationService.*;
import app.domain.model.Role;
import app.domain.services.AuthenticationService.AuthenticatedUser;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para reportes y consultas
 *
 * Endpoints para diferentes tipos de reportes:
 * - GET /api/reports/billing/period: Reporte de facturación por período
 * - GET /api/reports/billing/patient/{patientIdCard}: Reporte de facturación por paciente
 * - GET /api/reports/patients/registrations: Reporte de registros de pacientes
 * - GET /api/reports/inventory: Reporte de inventario
 * - GET /api/reports/medical: Reporte médico por diagnósticos
 * - GET /api/reports/statistics: Estadísticas generales del sistema
 */
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class ReportController {

    private final ReportApplicationService reportApplicationService;

    public ReportController(ReportApplicationService reportApplicationService) {
        this.reportApplicationService = reportApplicationService;
    }

    /**
     * REPORTE DE FACTURACIÓN POR PERÍODO
     * Solo accesible para personal administrativo
     */
    @GetMapping("/billing/period")
    public ResponseEntity<CommonResponse<BillingReport>> getBillingReport(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (startDate == null || startDate.trim().isEmpty()) {
                CommonResponse<BillingReport> errorResponse = CommonResponse.error(
                    "Fecha de inicio es requerida", "START_DATE_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            if (endDate == null || endDate.trim().isEmpty()) {
                CommonResponse<BillingReport> errorResponse = CommonResponse.error(
                    "Fecha de fin es requerida", "END_DATE_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "ADMINISTRATIVE");

            CommonResponse<BillingReport> response = reportApplicationService.generateBillingReport(
                startDate.trim(), endDate.trim(), currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<BillingReport> errorResponse = CommonResponse.error(
                "Error interno del servidor al generar reporte de facturación", "BILLING_REPORT_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * REPORTE DE FACTURACIÓN POR PACIENTE
     * Solo accesible para personal administrativo
     */
    @GetMapping("/billing/patient/{patientIdCard}")
    public ResponseEntity<CommonResponse<PatientBillingReport>> getPatientBillingReport(
            @PathVariable String patientIdCard,
            @RequestParam(required = false) Integer year,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (patientIdCard == null || patientIdCard.trim().isEmpty()) {
                CommonResponse<PatientBillingReport> errorResponse = CommonResponse.error(
                    "Cédula del paciente es requerida", "PATIENT_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "ADMINISTRATIVE");

            CommonResponse<PatientBillingReport> response = reportApplicationService.generatePatientBillingReport(
                patientIdCard.trim(), year, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<PatientBillingReport> errorResponse = CommonResponse.error(
                "Error interno del servidor al generar reporte de facturación del paciente", "PATIENT_BILLING_REPORT_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * REPORTE DE REGISTROS DE PACIENTES
     * Solo accesible para personal administrativo
     */
    @GetMapping("/patients/registrations")
    public ResponseEntity<CommonResponse<PatientRegistrationReport>> getPatientRegistrationReport(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (startDate == null || startDate.trim().isEmpty()) {
                CommonResponse<PatientRegistrationReport> errorResponse = CommonResponse.error(
                    "Fecha de inicio es requerida", "START_DATE_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            if (endDate == null || endDate.trim().isEmpty()) {
                CommonResponse<PatientRegistrationReport> errorResponse = CommonResponse.error(
                    "Fecha de fin es requerida", "END_DATE_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "ADMINISTRATIVE");

            CommonResponse<PatientRegistrationReport> response = reportApplicationService.generatePatientRegistrationReport(
                startDate.trim(), endDate.trim(), currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<PatientRegistrationReport> errorResponse = CommonResponse.error(
                "Error interno del servidor al generar reporte de registros de pacientes", "PATIENT_REGISTRATION_REPORT_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * REPORTE DE INVENTARIO
     * Solo accesible para personal de soporte
     */
    @GetMapping("/inventory")
    public ResponseEntity<CommonResponse<InventoryReport>> getInventoryReport(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            CommonResponse<InventoryReport> response = reportApplicationService.generateInventoryReport(currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryReport> errorResponse = CommonResponse.error(
                "Error interno del servidor al generar reporte de inventario", "INVENTORY_REPORT_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * REPORTE MÉDICO POR DIAGNÓSTICOS
     * Solo accesible para personal médico
     */
    @GetMapping("/medical")
    public ResponseEntity<CommonResponse<MedicalReport>> getMedicalReport(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (startDate == null || startDate.trim().isEmpty()) {
                CommonResponse<MedicalReport> errorResponse = CommonResponse.error(
                    "Fecha de inicio es requerida", "START_DATE_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            if (endDate == null || endDate.trim().isEmpty()) {
                CommonResponse<MedicalReport> errorResponse = CommonResponse.error(
                    "Fecha de fin es requerida", "END_DATE_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "DOCTOR");

            CommonResponse<MedicalReport> response = reportApplicationService.generateMedicalReport(
                startDate.trim(), endDate.trim(), currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<MedicalReport> errorResponse = CommonResponse.error(
                "Error interno del servidor al generar reporte médico", "MEDICAL_REPORT_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * ESTADÍSTICAS GENERALES DEL SISTEMA
     * Solo accesible para personal administrativo
     */
    @GetMapping("/statistics")
    public ResponseEntity<CommonResponse<SystemStatistics>> getSystemStatistics(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "ADMINISTRATIVE");

            CommonResponse<SystemStatistics> response = reportApplicationService.generateSystemStatistics(currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<SystemStatistics> errorResponse = CommonResponse.error(
                "Error interno del servidor al generar estadísticas del sistema", "SYSTEM_STATISTICS_500");
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
}