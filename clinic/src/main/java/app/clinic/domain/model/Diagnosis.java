package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a medical diagnosis.
 */
public class Diagnosis {
    private final String value;

    private Diagnosis(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Diagnosis cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static Diagnosis of(String value) {
        return new Diagnosis(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Diagnosis diagnosis = (Diagnosis) o;
        return Objects.equals(value, diagnosis.value);
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