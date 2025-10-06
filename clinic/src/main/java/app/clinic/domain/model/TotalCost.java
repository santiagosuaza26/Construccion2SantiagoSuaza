package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing the total cost of medical orders.
 * Calculated by summing up all individual costs in an order.
 */
public class TotalCost {
    private final Money value;

    private TotalCost(Money value) {
        this.value = value;
    }

    public static TotalCost of(Money value) {
        return new TotalCost(value);
    }

    public static TotalCost zero() {
        return new TotalCost(Money.of(0));
    }

    public Money getValue() {
        return value;
    }

    public TotalCost add(Money amount) {
        return new TotalCost(this.value.add(amount));
    }

    public TotalCost add(TotalCost other) {
        return new TotalCost(this.value.add(other.value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TotalCost totalCost = (TotalCost) o;
        return Objects.equals(value, totalCost.value);
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