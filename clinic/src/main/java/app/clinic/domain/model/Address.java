package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing an address.
 * Validates maximum 30 characters as per requirements.
 */
public class Address {
    private final String value;

    private Address(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
        String trimmedValue = value.trim();
        if (trimmedValue.length() > 30) {
            throw new IllegalArgumentException("Address cannot exceed 30 characters: " + trimmedValue);
        }
        this.value = trimmedValue;
    }

    public static Address of(String value) {
        return new Address(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(value, address.value);
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