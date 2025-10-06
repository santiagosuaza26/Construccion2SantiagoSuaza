package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a patient's address.
 * Validates maximum 30 characters as per requirements.
 */
public class PatientAddress {
    private final String value;

    private PatientAddress(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient address cannot be null or empty");
        }
        String trimmedValue = value.trim();
        if (trimmedValue.length() > 30) {
            throw new IllegalArgumentException("Patient address cannot exceed 30 characters: " + trimmedValue);
        }
        this.value = trimmedValue;
    }

    public static PatientAddress of(String value) {
        return new PatientAddress(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientAddress that = (PatientAddress) o;
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