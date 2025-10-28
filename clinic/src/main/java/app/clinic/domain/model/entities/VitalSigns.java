package app.clinic.domain.model.entities;

import java.time.LocalDateTime;

public class VitalSigns {
    private final String patientIdentificationNumber;
    private final LocalDateTime dateTime;
    private final String bloodPressure;
    private final double temperature;
    private final int pulse;
    private final int oxygenLevel;
    private final String observations;

    public VitalSigns(String patientIdentificationNumber, LocalDateTime dateTime, String bloodPressure, double temperature, int pulse, int oxygenLevel) {
        this(patientIdentificationNumber, dateTime, bloodPressure, temperature, pulse, oxygenLevel, "");
    }

    public VitalSigns(String patientIdentificationNumber, LocalDateTime dateTime, String bloodPressure, double temperature, int pulse, int oxygenLevel, String observations) {
        if (patientIdentificationNumber == null || patientIdentificationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient identification number cannot be null or empty");
        }
        if (dateTime == null) {
            throw new IllegalArgumentException("Date and time cannot be null");
        }
        if (bloodPressure == null || bloodPressure.trim().isEmpty()) {
            throw new IllegalArgumentException("Blood pressure cannot be null or empty");
        }
        if (temperature < 30.0 || temperature > 45.0) {
            throw new IllegalArgumentException("Temperature must be between 30.0 and 45.0 degrees");
        }
        if (pulse < 0 || pulse > 300) {
            throw new IllegalArgumentException("Pulse must be between 0 and 300");
        }
        if (oxygenLevel < 0 || oxygenLevel > 100) {
            throw new IllegalArgumentException("Oxygen level must be between 0 and 100");
        }
        if (observations == null) {
            observations = "";
        }
        this.patientIdentificationNumber = patientIdentificationNumber;
        this.dateTime = dateTime;
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.pulse = pulse;
        this.oxygenLevel = oxygenLevel;
        this.observations = observations;
    }

    public String getPatientIdentificationNumber() {
        return patientIdentificationNumber;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getPulse() {
        return pulse;
    }

    public int getOxygenLevel() {
        return oxygenLevel;
    }

    public String getObservations() {
        return observations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VitalSigns that = (VitalSigns) o;
        return patientIdentificationNumber.equals(that.patientIdentificationNumber) && dateTime.equals(that.dateTime);
    }

    @Override
    public int hashCode() {
        return patientIdentificationNumber.hashCode() + dateTime.hashCode();
    }

    @Override
    public String toString() {
        return "VitalSigns for " + patientIdentificationNumber + " at " + dateTime;
    }
}