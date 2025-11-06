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
public class AddDiagnosticsRequest {
    @NotBlank(message = "Diagnostic aid ID is required")
    private String diagnosticAidId;

    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost must be greater than 0")
    private Double cost;
}