package app.domain.model;

import java.time.LocalDate;

public class MedicalInsurance {

    private String companyName;
    private String policyNumber;
    private boolean active;
    private LocalDate expirationDate;

    public MedicalInsurance() {
    }

    public MedicalInsurance(String companyName, String policyNumber, boolean active, LocalDate expirationDate) {
        this.companyName = companyName;
        this.policyNumber = policyNumber;
        this.active = active;
        this.expirationDate = expirationDate;
    }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "MedicalInsurance{" +
                "companyName='" + companyName + '\'' +
                ", policyNumber='" + policyNumber + '\'' +
                ", active=" + active +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
