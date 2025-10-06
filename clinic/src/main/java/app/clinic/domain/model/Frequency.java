package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing the frequency of procedures or treatments.
 */
public class Frequency {
    private final String value;

    private Frequency(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Frequency cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static Frequency of(String value) {
        return new Frequency(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Frequency frequency = (Frequency) o;
        return Objects.equals(value, frequency.value);
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