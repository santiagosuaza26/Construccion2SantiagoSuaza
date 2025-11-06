package app.clinic.infrastructure.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterVisitRequest {
    @NotBlank(message = "Patient ID is required")
    private String patientId;

    @NotBlank(message = "Nurse ID is required")
    private String nurseId;

    @NotNull(message = "Visit date is required")
    private LocalDate visitDate;
}