package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a patient's insurance policy.
 * Contains insurance company, policy number, status, and expiration date.
 */
public class InsurancePolicy {
    private final InsuranceCompanyName companyName;
    private final PolicyNumber policyNumber;
    private final PolicyStatus status;
    private final PolicyExpirationDate expirationDate;

    private InsurancePolicy(InsuranceCompanyName companyName, PolicyNumber policyNumber,
                           PolicyStatus status, PolicyExpirationDate expirationDate) {
        this.companyName = companyName;
        this.policyNumber = policyNumber;
        this.status = status;
        this.expirationDate = expirationDate;
    }

    public static InsurancePolicy of(InsuranceCompanyName companyName, PolicyNumber policyNumber,
                                    PolicyStatus status, PolicyExpirationDate expirationDate) {
        return new InsurancePolicy(companyName, policyNumber, status, expirationDate);
    }

    public InsuranceCompanyName getCompanyName() {
        return companyName;
    }

    public PolicyNumber getPolicyNumber() {
        return policyNumber;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public PolicyExpirationDate getExpirationDate() {
        return expirationDate;
    }

    public boolean isActive() {
        return status.isActive() && !expirationDate.isExpired();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsurancePolicy that = (InsurancePolicy) o;
        return Objects.equals(companyName, that.companyName) &&
               Objects.equals(policyNumber, that.policyNumber) &&
               status == that.status &&
               Objects.equals(expirationDate, that.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, policyNumber, status, expirationDate);
    }

    @Override
    public String toString() {
        return String.format("InsurancePolicy{company=%s, policyNumber=%s, status=%s, expiration=%s}",
                           companyName, policyNumber, status, expirationDate);
    }
}