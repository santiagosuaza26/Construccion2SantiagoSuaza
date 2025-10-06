package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing vital signs data recorded by nurses.
 * Contains blood pressure, temperature, pulse, and oxygen level.
 */
public class VitalSignsData {
    private final BloodPressure bloodPressure;
    private final Temperature temperature;
    private final Pulse pulse;
    private final OxygenLevel oxygenLevel;

    private VitalSignsData(BloodPressure bloodPressure, Temperature temperature,
                          Pulse pulse, OxygenLevel oxygenLevel) {
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.pulse = pulse;
        this.oxygenLevel = oxygenLevel;
    }

    public static VitalSignsData of(BloodPressure bloodPressure, Temperature temperature,
                                   Pulse pulse, OxygenLevel oxygenLevel) {
        return new VitalSignsData(bloodPressure, temperature, pulse, oxygenLevel);
    }

    public BloodPressure getBloodPressure() {
        return bloodPressure;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public Pulse getPulse() {
        return pulse;
    }

    public OxygenLevel getOxygenLevel() {
        return oxygenLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VitalSignsData that = (VitalSignsData) o;
        return Objects.equals(bloodPressure, that.bloodPressure) &&
               Objects.equals(temperature, that.temperature) &&
               Objects.equals(pulse, that.pulse) &&
               Objects.equals(oxygenLevel, that.oxygenLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bloodPressure, temperature, pulse, oxygenLevel);
    }

    @Override
    public String toString() {
        return String.format("VitalSignsData{bloodPressure=%s, temperature=%s, pulse=%s, oxygenLevel=%s}",
                           bloodPressure, temperature, pulse, oxygenLevel);
    }
}