package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing an insurance policy number.
 */
public class PolicyNumber {
    private final String value;

    private PolicyNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Policy number cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static PolicyNumber of(String value) {
        return new PolicyNumber(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolicyNumber that = (PolicyNumber) o;
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