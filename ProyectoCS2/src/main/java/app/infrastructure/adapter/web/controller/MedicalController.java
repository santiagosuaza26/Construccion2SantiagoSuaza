package app.infrastructure.adapter.web.controller;

import app.application.dto.request.CreateOrderRequest;
import app.application.dto.request.UpdateClinicalHistoryRequest;
import app.application.dto.request.VitalSignsRequest;
import app.application.dto.response.ClinicalHistoryResponse;
import app.application.dto.response.CommonResponse;
import app.application.dto.response.OrderResponse;
import app.application.service.MedicalApplicationService;
import app.domain.model.Role;
import app.domain.services.AuthenticationService.AuthenticatedUser;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para funcionalidades médicas
 *
 * Endpoints para médicos y personal médico:
 * - POST /api/medical/orders: Crear órdenes médicas
 * - GET /api/medical/orders/{orderNumber}: Obtener orden por número
 * - POST /api/medical/clinical-history: Actualizar historia clínica
 * - GET /api/medical/clinical-history/{patientIdCard}: Obtener historia clínica
 * - POST /api/medical/vital-signs: Registrar signos vitales
 * - POST /api/medical/follow-up: Procesar consulta de seguimiento
 */
@RestController
@RequestMapping("/api/medical")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class MedicalController {

    private final MedicalApplicationService medicalApplicationService;

    public MedicalController(MedicalApplicationService medicalApplicationService) {
        this.medicalApplicationService = medicalApplicationService;
    }

    /**
     * Crear nueva orden médica
     * Solo accesible para médicos
     */
    @PostMapping("/orders")
    public ResponseEntity<CommonResponse<OrderResponse>> createMedicalOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "DOCTOR");

            CommonResponse<OrderResponse> response = medicalApplicationService.createMedicalOrder(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<OrderResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al crear orden médica", "MEDICAL_ORDER_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtener orden médica por número
     * Accesible para médicos, enfermeras y personal administrativo
     */
    @GetMapping("/orders/{orderNumber}")
    public ResponseEntity<CommonResponse<OrderResponse>> getMedicalOrder(
            @PathVariable String orderNumber,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (orderNumber == null || orderNumber.trim().isEmpty()) {
                CommonResponse<OrderResponse> errorResponse = CommonResponse.error(
                    "Número de orden es requerido", "ORDER_NUMBER_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "DOCTOR");

            CommonResponse<OrderResponse> response = medicalApplicationService.getOrderById(orderNumber.trim(), currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<OrderResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener orden médica", "GET_ORDER_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Actualizar historia clínica
     * Solo accesible para médicos
     */
    @PostMapping("/clinical-history")
    public ResponseEntity<CommonResponse<String>> updateClinicalHistory(
            @Valid @RequestBody UpdateClinicalHistoryRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "DOCTOR");

            CommonResponse<String> response = medicalApplicationService.updateClinicalHistory(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<String> errorResponse = CommonResponse.error(
                "Error interno del servidor al actualizar historia clínica", "CLINICAL_HISTORY_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtener historia clínica de un paciente
     * Accesible para médicos y el propio paciente
     */
    @GetMapping("/clinical-history/{patientIdCard}")
    public ResponseEntity<CommonResponse<ClinicalHistoryResponse>> getClinicalHistory(
            @PathVariable String patientIdCard,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (patientIdCard == null || patientIdCard.trim().isEmpty()) {
                CommonResponse<ClinicalHistoryResponse> errorResponse = CommonResponse.error(
                    "Cédula del paciente es requerida", "PATIENT_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "DOCTOR");

            CommonResponse<ClinicalHistoryResponse> response = medicalApplicationService.getClinicalHistory(
                patientIdCard.trim(), currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<ClinicalHistoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener historia clínica", "GET_CLINICAL_HISTORY_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Registrar signos vitales
     * Accesible para médicos y enfermeras
     */
    @PostMapping("/vital-signs")
    public ResponseEntity<CommonResponse<String>> recordVitalSigns(
            @Valid @RequestBody VitalSignsRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "NURSE");

            CommonResponse<String> response = medicalApplicationService.recordVitalSigns(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<String> errorResponse = CommonResponse.error(
                "Error interno del servidor al registrar signos vitales", "VITAL_SIGNS_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Procesar consulta de seguimiento
     * Solo accesible para médicos
     */
    @PostMapping("/follow-up")
    public ResponseEntity<CommonResponse<String>> processFollowUpConsultation(
            @RequestParam String patientIdCard,
            @RequestParam String doctorIdCard,
            @RequestParam String diagnosis,
            @RequestParam(required = false) List<String> relatedOrderNumbers,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (patientIdCard == null || patientIdCard.trim().isEmpty()) {
                CommonResponse<String> errorResponse = CommonResponse.error(
                    "Cédula del paciente es requerida", "PATIENT_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            if (doctorIdCard == null || doctorIdCard.trim().isEmpty()) {
                CommonResponse<String> errorResponse = CommonResponse.error(
                    "Cédula del médico es requerida", "DOCTOR_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            if (diagnosis == null || diagnosis.trim().isEmpty()) {
                CommonResponse<String> errorResponse = CommonResponse.error(
                    "Diagnóstico es requerido", "DIAGNOSIS_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "DOCTOR");

            CommonResponse<String> response = medicalApplicationService.processFollowUpConsultation(
                patientIdCard.trim(), doctorIdCard.trim(), diagnosis.trim(),
                relatedOrderNumbers, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<String> errorResponse = CommonResponse.error(
                "Error interno del servidor al procesar consulta de seguimiento", "FOLLOW_UP_500");
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