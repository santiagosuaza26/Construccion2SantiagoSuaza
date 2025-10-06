package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing the number of times a procedure should be repeated.
 */
public class NumberOfTimes {
    private final int value;

    private NumberOfTimes(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Number of times must be positive: " + value);
        }
        this.value = value;
    }

    public static NumberOfTimes of(int value) {
        return new NumberOfTimes(value);
    }

    public static NumberOfTimes once() {
        return new NumberOfTimes(1);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberOfTimes that = (NumberOfTimes) o;
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