package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing an emergency contact's phone number.
 * Validates that it contains exactly 10 digits as per requirements.
 */
public class EmergencyContactPhoneNumber {
    private final String value;

    private EmergencyContactPhoneNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact phone number cannot be null or empty");
        }
        String digitsOnly = value.replaceAll("\\D", "");
        if (!isValidEmergencyPhoneNumber(digitsOnly)) {
            throw new IllegalArgumentException("Emergency contact phone number must contain exactly 10 digits: " + value);
        }
        this.value = digitsOnly;
    }

    public static EmergencyContactPhoneNumber of(String value) {
        return new EmergencyContactPhoneNumber(value);
    }

    private boolean isValidEmergencyPhoneNumber(String digitsOnly) {
        return digitsOnly.length() == 10;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmergencyContactPhoneNumber that = (EmergencyContactPhoneNumber) o;
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