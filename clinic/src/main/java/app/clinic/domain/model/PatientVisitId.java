package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for patient visits.
 */
public class PatientVisitId {
    private final String value;

    private PatientVisitId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient visit ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static PatientVisitId of(String value) {
        return new PatientVisitId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientVisitId that = (PatientVisitId) o;
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