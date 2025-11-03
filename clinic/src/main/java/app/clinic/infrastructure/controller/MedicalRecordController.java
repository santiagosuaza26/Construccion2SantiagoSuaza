package app.clinic.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.AddMedicalRecordUseCase;
import app.clinic.application.usecase.GetMedicalRecordUseCase;
import app.clinic.application.usecase.UpdateMedicalRecordUseCase;
import app.clinic.infrastructure.dto.MedicalRecordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/medical/records")
@Tag(name = "Medical Records", description = "API para gestión de registros médicos")
public class MedicalRecordController {

    private final GetMedicalRecordUseCase getMedicalRecordUseCase;
    private final AddMedicalRecordUseCase addMedicalRecordUseCase;
    private final UpdateMedicalRecordUseCase updateMedicalRecordUseCase;

    public MedicalRecordController(GetMedicalRecordUseCase getMedicalRecordUseCase,
                                  AddMedicalRecordUseCase addMedicalRecordUseCase,
                                  UpdateMedicalRecordUseCase updateMedicalRecordUseCase) {
        this.getMedicalRecordUseCase = getMedicalRecordUseCase;
        this.addMedicalRecordUseCase = addMedicalRecordUseCase;
        this.updateMedicalRecordUseCase = updateMedicalRecordUseCase;
    }

    @GetMapping("/{patientId}")
    @Operation(summary = "Obtener registro médico completo", description = "Obtiene el registro médico completo de un paciente")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecord(@PathVariable @NotBlank String patientId) {
        getMedicalRecordUseCase.execute(patientId);

        // Convert domain entity to DTO (simplified for demo)
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setPatientId(patientId);
        // Note: Full conversion would require mapping all record entries

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{patientId}")
    @Operation(summary = "Agregar entrada al registro médico", description = "Agrega una nueva entrada al registro médico de un paciente")
    public ResponseEntity<Void> addMedicalRecord(@PathVariable @NotBlank String patientId,
                                                @Valid @RequestBody AddMedicalRecordRequest request) {
        addMedicalRecordUseCase.execute(patientId, request.doctorId, request.reason, request.symptoms, request.diagnosis);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{patientId}")
    @Operation(summary = "Actualizar registro médico", description = "Actualiza el registro médico agregando una nueva entrada")
    public ResponseEntity<Void> updateMedicalRecord(@PathVariable @NotBlank String patientId,
                                                   @Valid @RequestBody UpdateMedicalRecordRequest request) {
        updateMedicalRecordUseCase.execute(patientId, request.doctorId, request.reason, request.symptoms, request.diagnosis);
        return ResponseEntity.ok().build();
    }

    // Request DTOs
    public static class AddMedicalRecordRequest {
        @NotBlank(message = "El ID del doctor no puede estar vacío")
        public String doctorId;

        @NotBlank(message = "La razón de la consulta no puede estar vacía")
        public String reason;

        @NotBlank(message = "Los síntomas no pueden estar vacíos")
        public String symptoms;

        @NotBlank(message = "El diagnóstico no puede estar vacío")
        public String diagnosis;
    }

    public static class UpdateMedicalRecordRequest {
        @NotBlank(message = "El ID del doctor no puede estar vacío")
        public String doctorId;

        @NotBlank(message = "La razón de la consulta no puede estar vacía")
        public String reason;

        @NotBlank(message = "Los síntomas no pueden estar vacíos")
        public String symptoms;

        @NotBlank(message = "El diagnóstico no puede estar vacío")
        public String diagnosis;
    }
}