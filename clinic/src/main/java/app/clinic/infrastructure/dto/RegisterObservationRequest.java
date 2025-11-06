package app.clinic.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterObservationRequest {
    @NotBlank(message = "Patient ID is required")
    private String patientId;

    @NotBlank(message = "Nurse ID is required")
    private String nurseId;

    @NotBlank(message = "Observation is required")
    private String observation;
}