package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for procedure items within orders.
 */
public class ProcedureItemId {
    private final String value;

    private ProcedureItemId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Procedure item ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static ProcedureItemId of(String value) {
        return new ProcedureItemId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcedureItemId that = (ProcedureItemId) o;
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