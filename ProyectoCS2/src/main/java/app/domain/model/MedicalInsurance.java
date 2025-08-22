package app.domain.model;

import java.time.LocalDate;

public class MedicalInsurance {
    private String companyName;
    private String policyNumber;
    private boolean isActive;
    private LocalDate policyEndDate;

    public MedicalInsurance(String companyName, String policyNumber, boolean isActive, LocalDate policyEndDate) {
        this.companyName = companyName;
        this.policyNumber = policyNumber;
        this.isActive = isActive;
        this.policyEndDate = policyEndDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDate getPolicyEndDate() {
        return policyEndDate;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setPolicyEndDate(LocalDate policyEndDate) {
        this.policyEndDate = policyEndDate;
    }
    @Override
    public String toString() {
        return "MedicalInsurance{" +
                "companyName='" + companyName + '\'' +
                ", policyNumber='" + policyNumber + '\'' +
                ", isActive=" + isActive +
                ", policyEndDate=" + policyEndDate +
                '}';
    }
}
