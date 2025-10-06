package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing an identification number (Cedula).
 * Ensures uniqueness across the application and validates format.
 */
public class Cedula {
    private final String value;

    private Cedula(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Cedula cannot be null or empty");
        }
        if (!isValidCedula(value)) {
            throw new IllegalArgumentException("Invalid cedula format: " + value);
        }
        this.value = value.trim();
    }

    public static Cedula of(String value) {
        return new Cedula(value);
    }

    private boolean isValidCedula(String cedula) {
        // Remove any non-digit characters for validation
        String digitsOnly = cedula.replaceAll("\\D", "");

        // Must have between 5 and 10 digits
        if (digitsOnly.length() < 5 || digitsOnly.length() > 10) {
            return false;
        }

        // Check if all characters are digits
        return cedula.matches("\\d{5,10}");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cedula cedula = (Cedula) o;
        return Objects.equals(value, cedula.value);
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