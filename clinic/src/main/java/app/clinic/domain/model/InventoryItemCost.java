package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing the cost of an inventory item.
 * Wraps Money value object for better semantic meaning in inventory context.
 */
public class InventoryItemCost {
    private final Money value;

    private InventoryItemCost(Money value) {
        this.value = value;
    }

    public static InventoryItemCost of(Money value) {
        return new InventoryItemCost(value);
    }

    public Money getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryItemCost that = (InventoryItemCost) o;
        return Objects.equals(value, that.value);
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