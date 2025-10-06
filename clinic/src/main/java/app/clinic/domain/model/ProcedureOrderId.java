package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for procedure orders.
 */
public class ProcedureOrderId {
    private final String value;

    private ProcedureOrderId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Procedure order ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static ProcedureOrderId of(String value) {
        return new ProcedureOrderId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcedureOrderId that = (ProcedureOrderId) o;
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