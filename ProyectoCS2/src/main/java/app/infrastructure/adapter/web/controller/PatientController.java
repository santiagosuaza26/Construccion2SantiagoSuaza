package app.presentation.controller;

import app.application.dto.request.RegisterPatientRequest;
import app.application.dto.response.CommonResponse;
import app.application.dto.response.PatientResponse;
import app.application.service.PatientApplicationService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de pacientes
 *
 * Endpoints:
 * - GET /api/patients: Listar pacientes
 * - GET /api/patients/{idCard}: Obtener paciente por ID
 * - POST /api/patients: Registrar nuevo paciente
 * - PUT /api/patients/{idCard}: Actualizar paciente
 * - DELETE /api/patients/{idCard}: Eliminar paciente
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

            // TODO: Implementar búsqueda directa por ID cuando esté disponible
            CommonResponse<PatientResponse> errorResponse = CommonResponse.error(
                "Búsqueda de paciente no implementada - requiere autenticación", "PATIENT_SEARCH_501");
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(errorResponse);

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
            // TODO: Implementar registro con autenticación
            CommonResponse<PatientResponse> errorResponse = CommonResponse.error(
                "Registro de paciente no implementado - requiere autenticación administrativa", "PATIENT_REGISTER_501");
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(errorResponse);

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

            // TODO: Implementar actualización con autenticación
            CommonResponse<PatientResponse> errorResponse = CommonResponse.error(
                "Actualización de paciente no implementada - requiere autenticación administrativa", "PATIENT_UPDATE_501");
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(errorResponse);

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

            // TODO: Implementar eliminación con autenticación
            CommonResponse<String> errorResponse = CommonResponse.error(
                "Eliminación de paciente no implementada - requiere autenticación administrativa", "PATIENT_DELETE_501");
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(errorResponse);

        } catch (Exception e) {
            CommonResponse<String> errorResponse = CommonResponse.error(
                "Error interno del servidor al eliminar paciente", "PATIENT_DELETE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}