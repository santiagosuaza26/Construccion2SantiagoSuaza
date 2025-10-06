package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a user's identification number.
 * Must be unique across all users in the system.
 */
public class UserCedula {
    private final String value;

    private UserCedula(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("User cedula cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static UserCedula of(String value) {
        return new UserCedula(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCedula that = (UserCedula) o;
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