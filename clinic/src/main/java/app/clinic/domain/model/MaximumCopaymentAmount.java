package app.clinic.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object representing the maximum copayment amount per year.
 * Fixed at 1,000,000 pesos as per requirements.
 */
public class MaximumCopaymentAmount {
    private static final BigDecimal FIXED_AMOUNT = BigDecimal.valueOf(1_000_000);
    private final BigDecimal value;

    private MaximumCopaymentAmount() {
        this.value = FIXED_AMOUNT;
    }

    public static MaximumCopaymentAmount standard() {
        return new MaximumCopaymentAmount();
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaximumCopaymentAmount that = (MaximumCopaymentAmount) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "$" + value.toString();
    }
}