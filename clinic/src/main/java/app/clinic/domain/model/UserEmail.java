package app.clinic.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing a user's email address.
 * Validates email format and ensures it contains @ symbol and valid domain.
 */
public class UserEmail {
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final String value;

    private UserEmail(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }
        String trimmedValue = value.trim();
        if (!isValidEmail(trimmedValue)) {
            throw new IllegalArgumentException("Invalid email format: " + trimmedValue);
        }
        this.value = trimmedValue.toLowerCase();
    }

    public static UserEmail of(String value) {
        return new UserEmail(value);
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches() && email.contains("@");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEmail userEmail = (UserEmail) o;
        return Objects.equals(value, userEmail.value);
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