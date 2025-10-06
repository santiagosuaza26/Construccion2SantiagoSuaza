package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a patient's phone number.
 * Validates that it contains exactly 10 digits as per requirements.
 */
public class PatientPhoneNumber {
    private final String value;

    private PatientPhoneNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient phone number cannot be null or empty");
        }
        String digitsOnly = value.replaceAll("\\D", "");
        if (!isValidPatientPhoneNumber(digitsOnly)) {
            throw new IllegalArgumentException("Patient phone number must contain exactly 10 digits: " + value);
        }
        this.value = digitsOnly;
    }

    public static PatientPhoneNumber of(String value) {
        return new PatientPhoneNumber(value);
    }

    private boolean isValidPatientPhoneNumber(String digitsOnly) {
        return digitsOnly.length() == 10;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientPhoneNumber that = (PatientPhoneNumber) o;
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
