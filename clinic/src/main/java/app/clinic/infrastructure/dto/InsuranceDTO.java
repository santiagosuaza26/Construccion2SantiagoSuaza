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
public class InsuranceDTO {
    @NotBlank(message = "Insurance name is required")
    private String name;

    @NotBlank(message = "Policy number is required")
    private String policyNumber;

    @NotNull(message = "Active status is required")
    private Boolean active;

    @NotNull(message = "End date is required")
    private LocalDate endDate;
}