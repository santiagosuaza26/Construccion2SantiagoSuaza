package app.clinic.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object representing body temperature in Celsius.
 * Validates normal temperature range (35-42°C).
 */
public class Temperature {
    private final BigDecimal value;

    private Temperature(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Temperature cannot be null");
        }
        if (!isValidTemperature(value)) {
            throw new IllegalArgumentException("Temperature must be between 35°C and 42°C: " + value);
        }
        this.value = value;
    }

    public static Temperature of(BigDecimal value) {
        return new Temperature(value);
    }

    public static Temperature of(double value) {
        return new Temperature(BigDecimal.valueOf(value));
    }

    private boolean isValidTemperature(BigDecimal temperature) {
        BigDecimal min = BigDecimal.valueOf(35.0);
        BigDecimal max = BigDecimal.valueOf(42.0);
        return temperature.compareTo(min) >= 0 && temperature.compareTo(max) <= 0;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Temperature that = (Temperature) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value + "°C";
    }
}