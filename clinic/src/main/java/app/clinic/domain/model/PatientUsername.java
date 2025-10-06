package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a patient's username.
 * Must be unique, maximum 15 characters, and contain only letters and numbers.
 */
public class PatientUsername {
    private final String value;

    private PatientUsername(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient username cannot be null or empty");
        }
        String trimmedValue = value.trim();
        if (!isValidUsername(trimmedValue)) {
            throw new IllegalArgumentException("Patient username must be maximum 15 characters and contain only letters and numbers: " + trimmedValue);
        }
        this.value = trimmedValue;
    }

    public static PatientUsername of(String value) {
        return new PatientUsername(value);
    }

    private boolean isValidUsername(String username) {
        if (username.length() > 15) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9]+$");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientUsername that = (PatientUsername) o;
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