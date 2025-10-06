package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for inventory items.
 */
public class InventoryItemId {
    private final String value;

    private InventoryItemId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Inventory item ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static InventoryItemId of(String value) {
        return new InventoryItemId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryItemId that = (InventoryItemId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}