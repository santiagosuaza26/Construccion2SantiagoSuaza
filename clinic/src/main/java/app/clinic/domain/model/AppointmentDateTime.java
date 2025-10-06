package app.clinic.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object representing appointment date and time.
 * Validates that the appointment is scheduled for the future.
 */
public class AppointmentDateTime {
    private final LocalDateTime value;

    private AppointmentDateTime(LocalDateTime value) {
        if (value == null) {
            throw new IllegalArgumentException("Appointment date and time cannot be null");
        }
        if (!isValidAppointmentDateTime(value)) {
            throw new IllegalArgumentException("Appointment must be scheduled for the future: " + value);
        }
        this.value = value;
    }

    public static AppointmentDateTime of(LocalDateTime value) {
        return new AppointmentDateTime(value);
    }

    private boolean isValidAppointmentDateTime(LocalDateTime appointmentDateTime) {
        return appointmentDateTime.isAfter(LocalDateTime.now());
    }

    public LocalDateTime getValue() {
        return value;
    }

    public boolean isInThePast() {
        return value.isBefore(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentDateTime that = (AppointmentDateTime) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}