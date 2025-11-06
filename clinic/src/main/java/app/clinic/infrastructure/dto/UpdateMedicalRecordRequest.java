package app.clinic.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMedicalRecordRequest {
    @NotBlank(message = "Doctor ID is required")
    private String doctorId;

    @NotBlank(message = "Reason is required")
    private String reason;

    @NotBlank(message = "Symptoms are required")
    private String symptoms;

    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;
}