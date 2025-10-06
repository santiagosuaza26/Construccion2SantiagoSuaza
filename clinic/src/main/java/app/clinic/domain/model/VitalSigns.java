package app.clinic.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object representing patient vital signs.
 * Contains blood pressure, temperature, pulse, and oxygen level.
 */
public class VitalSigns {
    private final String bloodPressure;
    private final BigDecimal temperature;
    private final int pulse;
    private final BigDecimal oxygenLevel;

    private VitalSigns(String bloodPressure, BigDecimal temperature, int pulse, BigDecimal oxygenLevel) {
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.pulse = pulse;
        this.oxygenLevel = oxygenLevel;
    }

    public static VitalSigns of(String bloodPressure, BigDecimal temperature, int pulse, BigDecimal oxygenLevel) {
        return new VitalSigns(bloodPressure, temperature, pulse, oxygenLevel);
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public int getPulse() {
        return pulse;
    }

    public BigDecimal getOxygenLevel() {
        return oxygenLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VitalSigns that = (VitalSigns) o;
        return pulse == that.pulse &&
               Objects.equals(bloodPressure, that.bloodPressure) &&
               Objects.equals(temperature, that.temperature) &&
               Objects.equals(oxygenLevel, that.oxygenLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bloodPressure, temperature, pulse, oxygenLevel);
    }

    @Override
    public String toString() {
        return String.format("VitalSigns{bloodPressure='%s', temperature=%s, pulse=%d, oxygenLevel=%s}",
                           bloodPressure, temperature, pulse, oxygenLevel);
    }
}