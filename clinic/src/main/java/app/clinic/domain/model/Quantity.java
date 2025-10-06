package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a quantity of items.
 * Must be positive for procedures and diagnostic aids.
 */
public class Quantity {
    private final int value;

    private Quantity(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Quantity must be positive: " + value);
        }
        this.value = value;
    }

    public static Quantity of(int value) {
        return new Quantity(value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}