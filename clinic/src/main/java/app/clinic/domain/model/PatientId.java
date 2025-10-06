package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for patients.
 */
public class PatientId {
    private final String value;

    private PatientId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static PatientId of(String value) {
        return new PatientId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientId patientId = (PatientId) o;
        return Objects.equals(value, patientId.value);
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