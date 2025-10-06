package app.clinic.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object representing blood oxygen level in percentage.
 * Validates normal oxygen saturation range (70-100%).
 */
public class OxygenLevel {
    private final BigDecimal value;

    private OxygenLevel(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Oxygen level cannot be null");
        }
        if (!isValidOxygenLevel(value)) {
            throw new IllegalArgumentException("Oxygen level must be between 70% and 100%: " + value);
        }
        this.value = value;
    }

    public static OxygenLevel of(BigDecimal value) {
        return new OxygenLevel(value);
    }

    public static OxygenLevel of(double value) {
        return new OxygenLevel(BigDecimal.valueOf(value));
    }

    private boolean isValidOxygenLevel(BigDecimal oxygenLevel) {
        BigDecimal min = BigDecimal.valueOf(70.0);
        BigDecimal max = BigDecimal.valueOf(100.0);
        return oxygenLevel.compareTo(min) >= 0 && oxygenLevel.compareTo(max) <= 0;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OxygenLevel that = (OxygenLevel) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value + "%";
    }
}