package app.clinic.infrastructure.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddMedicationsRequest {
    @NotBlank(message = "Medication ID is required")
    private String medicationId;

    @NotBlank(message = "Dosage is required")
    private String dosage;

    @NotBlank(message = "Duration is required")
    private String duration;

    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost must be greater than 0")
    private Double cost;
}