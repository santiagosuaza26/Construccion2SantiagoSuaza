package app.clinic.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object representing the cost of medical items or services.
 * Must be non-negative value.
 */
public class Cost {
    private final Money value;

    private Cost(Money value) {
        if (value == null) {
            throw new IllegalArgumentException("Cost cannot be null");
        }
        this.value = value;
    }

    public static Cost of(Money value) {
        return new Cost(value);
    }

    public static Cost of(BigDecimal amount) {
        return new Cost(Money.of(amount));
    }

    public static Cost of(double amount) {
        return new Cost(Money.of(amount));
    }

    public Money getValue() {
        return value;
    }

    public Cost add(Cost other) {
        return new Cost(this.value.add(other.value));
    }

    public Cost multiply(int factor) {
        return new Cost(this.value.multiply(factor));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cost cost = (Cost) o;
        return Objects.equals(value, cost.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}