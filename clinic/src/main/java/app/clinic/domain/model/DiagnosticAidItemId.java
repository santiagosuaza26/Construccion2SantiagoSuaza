package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for diagnostic aid items within orders.
 */
public class DiagnosticAidItemId {
    private final String value;

    private DiagnosticAidItemId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Diagnostic aid item ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static DiagnosticAidItemId of(String value) {
        return new DiagnosticAidItemId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiagnosticAidItemId that = (DiagnosticAidItemId) o;
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