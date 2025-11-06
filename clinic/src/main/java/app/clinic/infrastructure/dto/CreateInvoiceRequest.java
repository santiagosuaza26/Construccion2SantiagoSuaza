package app.clinic.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceRequest {
    @NotBlank(message = "Doctor name is required")
    private String doctorName;

    @NotBlank(message = "Insurance name is required")
    private String insuranceName;

    @NotBlank(message = "Policy number is required")
    private String policyNumber;

    @NotNull(message = "Patient data is required")
    private PatientDTO patientData;

    @NotBlank(message = "Services rendered is required")
    private String servicesRendered;
}