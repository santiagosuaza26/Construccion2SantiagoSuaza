package app.infrastructure.adapter.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.application.dto.request.RegisterPatientRequest;
import app.application.dto.response.CommonResponse;
import app.application.dto.response.PatientResponse;
import app.application.service.PatientApplicationService;
import app.domain.model.Role;
import app.domain.services.AuthenticationService.AuthenticatedUser;
import jakarta.validation.Valid;

/**
 * REST Controller for patient management
 *
 * Endpoints:
 * - GET /api/patients: List patients
 * - GET /api/patients/{idCard}: Get patient by ID
 * - POST /api/patients: Register new patient
 * - PUT /api/patients/{idCard}: Update patient
 * - DELETE /api/patients/{idCard}: Delete patient
 */
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class PatientController {

    private final PatientApplicationService patientApplicationService;

    public PatientController(PatientApplicationService patientApplicationService) {
        this.patientApplicationService = patientApplicationService;
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<PatientResponse>>> getAllPatients() {
        try {
            CommonResponse<List<PatientResponse>> response = patientApplicationService.getAllPatients();

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<List<PatientResponse>> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener pacientes", "PATIENTS_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{idCard}")
    public ResponseEntity<CommonResponse<PatientResponse>> getPatientById(
            @PathVariable String idCard) {

        try {
            if (idCard == null || idCard.trim().isEmpty()) {
                CommonResponse<PatientResponse> errorResponse = CommonResponse.error(
                    "ID de paciente es requerido", "PATIENT_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Obtener usuario autenticado del contexto de seguridad
            AuthenticatedUser currentUser = getCurrentAuthenticatedUser();

            CommonResponse<PatientResponse> response = patientApplicationService.getPatientById(idCard, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

        } catch (Exception e) {
            CommonResponse<PatientResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al buscar paciente", "PATIENT_SEARCH_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<CommonResponse<PatientResponse>> registerPatient(
            @Valid @RequestBody RegisterPatientRequest registerPatientRequest) {

        try {
            // TODO: Obtener usuario autenticado del contexto de seguridad
            AuthenticatedUser currentUser = getCurrentAuthenticatedUser();

            CommonResponse<PatientResponse> response = patientApplicationService.registerPatient(registerPatientRequest, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

        } catch (Exception e) {
            CommonResponse<PatientResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al registrar paciente", "PATIENT_REGISTER_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{idCard}")
    public ResponseEntity<CommonResponse<PatientResponse>> updatePatient(
            @PathVariable String idCard,
            @Valid @RequestBody RegisterPatientRequest updatePatientRequest) {

        try {
            if (idCard == null || idCard.trim().isEmpty()) {
                CommonResponse<PatientResponse> errorResponse = CommonResponse.error(
                    "ID de paciente es requerido", "PATIENT_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Obtener usuario autenticado del contexto de seguridad
            AuthenticatedUser currentUser = getCurrentAuthenticatedUser();

            CommonResponse<PatientResponse> response = patientApplicationService.updatePatient(idCard, updatePatientRequest, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

        } catch (Exception e) {
            CommonResponse<PatientResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al actualizar paciente", "PATIENT_UPDATE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{idCard}")
    public ResponseEntity<CommonResponse<String>> deletePatient(
            @PathVariable String idCard) {

        try {
            if (idCard == null || idCard.trim().isEmpty()) {
                CommonResponse<String> errorResponse = CommonResponse.error(
                    "ID de paciente es requerido", "PATIENT_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Obtener usuario autenticado del contexto de seguridad
            AuthenticatedUser currentUser = getCurrentAuthenticatedUser();

            CommonResponse<String> response = patientApplicationService.removePatient(idCard, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

        } catch (Exception e) {
            CommonResponse<String> errorResponse = CommonResponse.error(
                "Error interno del servidor al eliminar paciente", "PATIENT_DELETE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // =============================================================================
    // MÉTODOS PRIVADOS DE UTILIDAD
    // =============================================================================

    /**
     * Obtiene el usuario actualmente autenticado
     * TODO: Implementar con Spring Security cuando esté disponible
     */
    private AuthenticatedUser getCurrentAuthenticatedUser() {
        // Implementación temporal - en producción usar Spring Security
        // Para fines de demostración, creamos un usuario administrativo temporal
        return new AuthenticatedUser(
            "admin001",
            "Administrador Sistema",
            Role.ADMINISTRATIVE,
            true // isStaff
        );
    }
}