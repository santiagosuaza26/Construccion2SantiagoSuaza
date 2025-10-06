package app.clinic.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object representing a copayment amount in Colombian Pesos.
 * Fixed at 50,000 pesos as per requirements.
 */
public class CopaymentAmount {
    private static final BigDecimal FIXED_AMOUNT = BigDecimal.valueOf(50_000);
    private final BigDecimal value;

    private CopaymentAmount() {
        this.value = FIXED_AMOUNT;
    }

    public static CopaymentAmount standard() {
        return new CopaymentAmount();
    }

    public static CopaymentAmount of(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Copayment amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Copayment amount cannot be negative");
        }
        // In this implementation, we ignore the parameter and always return the standard amount
        // as per the requirement that copayment is fixed at 50,000 pesos
        return new CopaymentAmount();
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CopaymentAmount that = (CopaymentAmount) o;
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