package app.clinic.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object representing accumulated copayment amount for a patient in a year.
 */
public class AccumulatedCopayment {
    private final BigDecimal value;

    private AccumulatedCopayment(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Accumulated copayment cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Accumulated copayment cannot be negative");
        }
        this.value = value;
    }

    public static AccumulatedCopayment of(BigDecimal value) {
        return new AccumulatedCopayment(value);
    }

    public static AccumulatedCopayment zero() {
        return new AccumulatedCopayment(BigDecimal.ZERO);
    }

    public AccumulatedCopayment add(Money amount) {
        return new AccumulatedCopayment(this.value.add(amount.getAmount()));
    }

    public boolean hasReachedMaximum(Money maximumAmount) {
        return this.value.compareTo(maximumAmount.getAmount()) >= 0;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccumulatedCopayment that = (AccumulatedCopayment) o;
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