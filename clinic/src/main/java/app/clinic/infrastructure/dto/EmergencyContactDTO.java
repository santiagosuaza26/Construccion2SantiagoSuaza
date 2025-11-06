package app.clinic.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Relationship is required")
    private String relationship;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "\\d{10}", message = "Phone must be exactly 10 digits")
    private String phone;
}