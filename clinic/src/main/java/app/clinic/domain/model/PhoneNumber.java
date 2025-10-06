package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a phone number.
 * Validates that the phone number contains between 1 and 10 digits.
 */
public class PhoneNumber {
    private final String value;

    private PhoneNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        String digitsOnly = value.replaceAll("\\D", "");
        if (!isValidPhoneNumber(digitsOnly)) {
            throw new IllegalArgumentException("Phone number must contain between 1 and 10 digits: " + value);
        }
        this.value = digitsOnly;
    }

    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }

    private boolean isValidPhoneNumber(String digitsOnly) {
        return !digitsOnly.isEmpty() && digitsOnly.length() <= 10;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
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