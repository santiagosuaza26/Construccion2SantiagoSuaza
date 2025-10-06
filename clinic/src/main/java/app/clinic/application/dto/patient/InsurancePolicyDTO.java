package app.clinic.application.dto.patient;

/**
 * Data Transfer Object for Insurance Policy information.
 * Used for API responses containing insurance policy data.
 */
public class InsurancePolicyDTO {
    private String companyName;
    private String policyNumber;
    private String status;
    private String expirationDate;
    private boolean active;

    // Default constructor
    public InsurancePolicyDTO() {}

    // Constructor with parameters
    public InsurancePolicyDTO(String companyName, String policyNumber, String status,
                             String expirationDate, boolean active) {
        this.companyName = companyName;
        this.policyNumber = policyNumber;
        this.status = status;
        this.expirationDate = expirationDate;
        this.active = active;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("InsurancePolicyDTO{companyName='%s', policyNumber='%s', status='%s', active=%s}",
                           companyName, policyNumber, status, active);
    }
}