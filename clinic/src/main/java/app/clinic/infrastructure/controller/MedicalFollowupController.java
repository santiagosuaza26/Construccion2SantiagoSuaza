package app.clinic.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.AddDiagnosticFollowupUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/medical/followup")
@Tag(name = "Medical Followup", description = "API para seguimiento médico y diagnósticos")
public class MedicalFollowupController {

    private final AddDiagnosticFollowupUseCase addDiagnosticFollowupUseCase;

    public MedicalFollowupController(AddDiagnosticFollowupUseCase addDiagnosticFollowupUseCase) {
        this.addDiagnosticFollowupUseCase = addDiagnosticFollowupUseCase;
    }

    @PostMapping("/diagnostic")
    @Operation(summary = "Agregar seguimiento diagnóstico", description = "Agrega un seguimiento diagnóstico con medicación")
    public ResponseEntity<Void> addDiagnosticFollowup(@Valid @RequestBody AddDiagnosticFollowupRequest request) {
        addDiagnosticFollowupUseCase.execute(request.patientId, request.diagnosis,
                                           request.orderNumber, request.medicationId,
                                           request.dosage, request.duration);
        return ResponseEntity.ok().build();
    }

    // Request DTO
    public static class AddDiagnosticFollowupRequest {
        @NotBlank(message = "El ID del paciente no puede estar vacío")
        public String patientId;

        @NotBlank(message = "El diagnóstico no puede estar vacío")
        public String diagnosis;

        @NotBlank(message = "El número de orden no puede estar vacío")
        public String orderNumber;

        @NotBlank(message = "El ID del medicamento no puede estar vacío")
        public String medicationId;

        @NotBlank(message = "La dosis no puede estar vacía")
        public String dosage;

        @NotBlank(message = "La duración no puede estar vacía")
        public String duration;
    }
}