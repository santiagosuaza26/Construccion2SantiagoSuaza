package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing medication dosage information.
 */
public class Dosage {
    private final String value;

    private Dosage(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Dosage cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static Dosage of(String value) {
        return new Dosage(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dosage dosage = (Dosage) o;
        return Objects.equals(value, dosage.value);
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