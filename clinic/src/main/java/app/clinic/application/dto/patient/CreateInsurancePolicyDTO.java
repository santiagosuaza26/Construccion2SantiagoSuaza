package app.clinic.application.dto.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for creating insurance policy information.
 * Used in patient creation and updates.
 */
public class CreateInsurancePolicyDTO {

    @NotBlank(message = "Insurance company name is required")
    @Size(max = 100, message = "Insurance company name must not exceed 100 characters")
    private String companyName;

    @NotBlank(message = "Policy number is required")
    @Size(max = 50, message = "Policy number must not exceed 50 characters")
    private String policyNumber;

    @NotBlank(message = "Policy status is required")
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "Policy status must be either ACTIVE or INACTIVE")
    private String status;

    @NotBlank(message = "Policy expiration date is required")
    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "Policy expiration date must be in DD/MM/YYYY format")
    private String expirationDate;

    // Default constructor
    public CreateInsurancePolicyDTO() {}

    // Constructor with parameters
    public CreateInsurancePolicyDTO(String companyName, String policyNumber, String status, String expirationDate) {
        this.companyName = companyName;
        this.policyNumber = policyNumber;
        this.status = status;
        this.expirationDate = expirationDate;
    }

    // Getters and Setters
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return String.format("CreateInsurancePolicyDTO{companyName='%s', policyNumber='%s', status='%s'}",
                           companyName, policyNumber, status);
    }
}