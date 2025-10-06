package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for diagnostic aid orders.
 */
public class DiagnosticAidOrderId {
    private final String value;

    private DiagnosticAidOrderId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Diagnostic aid order ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static DiagnosticAidOrderId of(String value) {
        return new DiagnosticAidOrderId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiagnosticAidOrderId that = (DiagnosticAidOrderId) o;
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