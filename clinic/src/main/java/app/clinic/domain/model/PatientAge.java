package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a patient's age.
 * Calculated from birth date and must be valid (not negative).
 */
public class PatientAge {
    private final int value;

    private PatientAge(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Age cannot be negative: " + value);
        }
        this.value = value;
    }

    public static PatientAge of(int value) {
        return new PatientAge(value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientAge that = (PatientAge) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}