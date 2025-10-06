package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing treatment duration for medications.
 */
public class TreatmentDuration {
    private final String value;

    private TreatmentDuration(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Treatment duration cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static TreatmentDuration of(String value) {
        return new TreatmentDuration(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreatmentDuration that = (TreatmentDuration) o;
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