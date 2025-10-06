package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for appointments.
 */
public class AppointmentId {
    private final String value;

    private AppointmentId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Appointment ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static AppointmentId of(String value) {
        return new AppointmentId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentId that = (AppointmentId) o;
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