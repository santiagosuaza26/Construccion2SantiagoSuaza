package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a user's address.
 * Validates maximum 30 characters as per requirements.
 */
public class UserAddress {
    private final String value;

    private UserAddress(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("User address cannot be null or empty");
        }
        String trimmedValue = value.trim();
        if (trimmedValue.length() > 30) {
            throw new IllegalArgumentException("User address cannot exceed 30 characters: " + trimmedValue);
        }
        this.value = trimmedValue;
    }

    public static UserAddress of(String value) {
        return new UserAddress(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAddress that = (UserAddress) o;
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