package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a user's phone number.
 * Validates that it contains between 1 and 10 digits as per requirements.
 */
public class UserPhoneNumber {
    private final String value;

    private UserPhoneNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("User phone number cannot be null or empty");
        }
        String digitsOnly = value.replaceAll("\\D", "");
        if (!isValidUserPhoneNumber(digitsOnly)) {
            throw new IllegalArgumentException("User phone number must contain between 1 and 10 digits: " + value);
        }
        this.value = digitsOnly;
    }

    public static UserPhoneNumber of(String value) {
        return new UserPhoneNumber(value);
    }

    private boolean isValidUserPhoneNumber(String digitsOnly) {
        return !digitsOnly.isEmpty() && digitsOnly.length() <= 10;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPhoneNumber that = (UserPhoneNumber) o;
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