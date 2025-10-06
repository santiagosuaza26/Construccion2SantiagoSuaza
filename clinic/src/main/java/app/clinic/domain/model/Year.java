package app.clinic.domain.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Value Object representing a year for copayment calculations.
 */
public class Year {
    private final int value;

    private Year(int value) {
        if (value < 1900 || value > 2100) {
            throw new IllegalArgumentException("Invalid year: " + value);
        }
        this.value = value;
    }

    public static Year of(int value) {
        return new Year(value);
    }

    public static Year current() {
        return new Year(LocalDate.now().getYear());
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Year year = (Year) o;
        return value == year.value;
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