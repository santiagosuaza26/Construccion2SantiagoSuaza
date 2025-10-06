package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for medication items within orders.
 */
public class MedicationItemId {
    private final String value;

    private MedicationItemId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Medication item ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static MedicationItemId of(String value) {
        return new MedicationItemId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationItemId that = (MedicationItemId) o;
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