package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing the result of billing calculations.
 * Contains total cost, copayment amount, insurance coverage, and patient responsibility.
 */
public class BillingCalculationResult {
    private final Money totalCost;
    private final Money copaymentAmount;
    private final Money insuranceCoverage;
    private final Money patientResponsibility;
    private final boolean requiresFullPayment;

    private BillingCalculationResult(Money totalCost, Money copaymentAmount, Money insuranceCoverage,
                                    Money patientResponsibility, boolean requiresFullPayment) {
        this.totalCost = totalCost;
        this.copaymentAmount = copaymentAmount;
        this.insuranceCoverage = insuranceCoverage;
        this.patientResponsibility = patientResponsibility;
        this.requiresFullPayment = requiresFullPayment;
    }

    public static BillingCalculationResult of(Money totalCost, Money copaymentAmount, Money insuranceCoverage,
                                             Money patientResponsibility, boolean requiresFullPayment) {
        return new BillingCalculationResult(totalCost, copaymentAmount, insuranceCoverage,
                                          patientResponsibility, requiresFullPayment);
    }

    public Money getTotalCost() {
        return totalCost;
    }

    public Money getCopaymentAmount() {
        return copaymentAmount;
    }

    public Money getInsuranceCoverage() {
        return insuranceCoverage;
    }

    public Money getPatientResponsibility() {
        return patientResponsibility;
    }

    public boolean requiresFullPayment() {
        return requiresFullPayment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillingCalculationResult that = (BillingCalculationResult) o;
        return requiresFullPayment == that.requiresFullPayment &&
               Objects.equals(totalCost, that.totalCost) &&
               Objects.equals(copaymentAmount, that.copaymentAmount) &&
               Objects.equals(insuranceCoverage, that.insuranceCoverage) &&
               Objects.equals(patientResponsibility, that.patientResponsibility);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalCost, copaymentAmount, insuranceCoverage, patientResponsibility, requiresFullPayment);
    }

    @Override
    public String toString() {
        return String.format("BillingCalculationResult{total=%s, copayment=%s, insurance=%s, patient=%s, fullPayment=%s}",
                           totalCost, copaymentAmount, insuranceCoverage, patientResponsibility, requiresFullPayment);
    }
}