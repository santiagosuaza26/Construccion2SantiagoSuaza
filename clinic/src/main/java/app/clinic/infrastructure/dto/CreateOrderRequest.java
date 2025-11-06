package app.clinic.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @NotBlank(message = "Order number is required")
    @Pattern(regexp = "\\d{1,6}", message = "Order number must be 1-6 digits")
    private String orderNumber;

    @NotBlank(message = "Patient ID is required")
    private String patientId;

    @NotBlank(message = "Doctor ID is required")
    private String doctorId;
}