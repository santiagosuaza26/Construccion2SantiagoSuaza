package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for patient record entries.
 */
public class PatientRecordEntryId {
    private final String value;

    private PatientRecordEntryId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient record entry ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static PatientRecordEntryId of(String value) {
        return new PatientRecordEntryId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRecordEntryId that = (PatientRecordEntryId) o;
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