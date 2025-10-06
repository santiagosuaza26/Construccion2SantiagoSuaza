package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing the name of an inventory item
 * (medication, procedure, or diagnostic aid).
 */
public class InventoryItemName {
    private final String value;

    private InventoryItemName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Inventory item name cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static InventoryItemName of(String value) {
        return new InventoryItemName(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryItemName that = (InventoryItemName) o;
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