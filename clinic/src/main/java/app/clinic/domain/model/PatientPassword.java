package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a patient's password.
 * Validates password strength requirements: uppercase, number, special character, minimum 8 characters.
 */
public class PatientPassword {
    private final String hashedValue;

    private PatientPassword(String hashedValue) {
        this.hashedValue = hashedValue;
    }

    /**
     * Creates a PatientPassword from plain text after validation.
     * Note: In production, this should hash the password before storing.
     */
    public static PatientPassword of(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (!isValidPassword(plainTextPassword)) {
            throw new IllegalArgumentException("Password must contain at least 8 characters, one uppercase letter, one number, and one special character");
        }
        // In production, hash the password here
        return new PatientPassword(plainTextPassword);
    }

    /**
     * Creates a PatientPassword from an already hashed value (for loading from database).
     */
    public static PatientPassword ofHashed(String hashedValue) {
        if (hashedValue == null || hashedValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        return new PatientPassword(hashedValue);
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
            } else if (isSpecialCharacter(c)) {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    private static boolean isSpecialCharacter(char c) {
        String specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        return specialChars.indexOf(c) != -1;
    }

    public String getHashedValue() {
        return hashedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientPassword that = (PatientPassword) o;
        return Objects.equals(hashedValue, that.hashedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashedValue);
    }

    @Override
    public String toString() {
        return "PatientPassword[****]";
    }
}