package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for patient medical records.
 */
public class PatientRecordId {
    private final String value;

    private PatientRecordId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient record ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static PatientRecordId of(String value) {
        return new PatientRecordId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRecordId that = (PatientRecordId) o;
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