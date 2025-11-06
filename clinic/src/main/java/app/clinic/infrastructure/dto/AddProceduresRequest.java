package app.clinic.infrastructure.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProceduresRequest {
    @NotBlank(message = "Procedure ID is required")
    private String procedureId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost must be greater than 0")
    private Double cost;

    @NotNull(message = "Requires specialist is required")
    private Boolean requiresSpecialist;
}