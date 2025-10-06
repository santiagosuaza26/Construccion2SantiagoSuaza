package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a doctor's identification number.
 * Must be unique and maximum 10 digits as per requirements.
 */
public class DoctorCedula {
    private final String value;

    private DoctorCedula(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor cedula cannot be null or empty");
        }
        String trimmedValue = value.trim();
        if (!isValidDoctorCedula(trimmedValue)) {
            throw new IllegalArgumentException("Doctor cedula must be maximum 10 digits: " + trimmedValue);
        }
        this.value = trimmedValue;
    }

    public static DoctorCedula of(String value) {
        return new DoctorCedula(value);
    }

    private boolean isValidDoctorCedula(String cedula) {
        if (cedula.length() > 10) {
            return false;
        }
        return cedula.matches("\\d+");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorCedula that = (DoctorCedula) o;
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