package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing patient symptoms description.
 */
public class Symptoms {
    private final String value;

    private Symptoms(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Symptoms cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static Symptoms of(String value) {
        return new Symptoms(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symptoms symptoms = (Symptoms) o;
        return Objects.equals(value, symptoms.value);
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