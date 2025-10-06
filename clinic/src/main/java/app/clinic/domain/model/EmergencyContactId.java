package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for emergency contacts.
 */
public class EmergencyContactId {
    private final String value;

    private EmergencyContactId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static EmergencyContactId of(String value) {
        return new EmergencyContactId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmergencyContactId that = (EmergencyContactId) o;
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