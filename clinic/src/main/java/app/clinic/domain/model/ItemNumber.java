package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing an item number within an order.
 * Items start from 1 and are sequential within each order.
 */
public class ItemNumber {
    private final int value;

    private ItemNumber(int value) {
        if (value < 1) {
            throw new IllegalArgumentException("Item number must be positive: " + value);
        }
        this.value = value;
    }

    public static ItemNumber of(int value) {
        return new ItemNumber(value);
    }

    public static ItemNumber first() {
        return new ItemNumber(1);
    }

    public ItemNumber next() {
        return new ItemNumber(this.value + 1);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemNumber that = (ItemNumber) o;
        return value == that.value;
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