package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a user's password.
 * Validates password strength requirements: uppercase, number, special character, minimum 8 characters.
 */
public class UserPassword {
    private final String hashedValue;

    private UserPassword(String hashedValue) {
        this.hashedValue = hashedValue;
    }

    /**
     * Creates a UserPassword from plain text after validation.
     * Note: In production, this should hash the password before storing.
     */
    public static UserPassword of(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (!isValidPassword(plainTextPassword)) {
            throw new IllegalArgumentException("Password must contain at least 8 characters, one uppercase letter, one number, and one special character");
        }
        // In production, hash the password here
        return new UserPassword(plainTextPassword);
    }

    /**
     * Creates a UserPassword from an already hashed value (for loading from database).
     */
    public static UserPassword ofHashed(String hashedValue) {
        if (hashedValue == null || hashedValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        return new UserPassword(hashedValue);
    }

    private static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isWhitespace(c)) {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    public String getHashedValue() {
        return hashedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPassword that = (UserPassword) o;
        return Objects.equals(hashedValue, that.hashedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashedValue);
    }

    @Override
    public String toString() {
        return "UserPassword[****]";
    }
}