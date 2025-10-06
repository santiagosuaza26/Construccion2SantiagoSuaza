package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a user's username.
 * Must be unique, maximum 15 characters, and contain only letters and numbers.
 */
public class UserUsername {
    private final String value;

    private UserUsername(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        String trimmedValue = value.trim();
        if (!isValidUsername(trimmedValue)) {
            throw new IllegalArgumentException("Username must be maximum 15 characters and contain only letters and numbers: " + trimmedValue);
        }
        this.value = trimmedValue;
    }

    public static UserUsername of(String value) {
        return new UserUsername(value);
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
        UserUsername that = (UserUsername) o;
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