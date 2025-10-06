package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a patient's identification number.
 * Must be unique across all patients in the system.
 */
public class PatientCedula {
    private final String value;

    private PatientCedula(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient cedula cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static PatientCedula of(String value) {
        return new PatientCedula(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientCedula that = (PatientCedula) o;
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