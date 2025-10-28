package app.clinic.domain.model.entities;

import java.time.LocalDate;

public class Insurance {
    private final String companyName;
    private final String policyNumber;
    private final boolean active;
    private final LocalDate validityDate;

    public Insurance(String companyName, String policyNumber, boolean active, LocalDate validityDate) {
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Policy number cannot be null or empty");
        }
        this.companyName = companyName;
        this.policyNumber = policyNumber;
        this.active = active;
        this.validityDate = validityDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDate getValidityDate() {
        return validityDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Insurance insurance = (Insurance) o;
        return active == insurance.active && companyName.equals(insurance.companyName) && policyNumber.equals(insurance.policyNumber) && validityDate.equals(insurance.validityDate);
    }

    @Override
    public int hashCode() {
        return companyName.hashCode() + policyNumber.hashCode() + Boolean.hashCode(active) + validityDate.hashCode();
    }

    @Override
    public String toString() {
        return companyName + " - " + policyNumber + " (Active: " + active + ")";
    }
}