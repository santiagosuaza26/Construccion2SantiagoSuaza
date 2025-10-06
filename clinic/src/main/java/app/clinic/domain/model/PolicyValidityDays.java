package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing the number of validity days for an insurance policy.
 */
public class PolicyValidityDays {
    private final int value;

    private PolicyValidityDays(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Policy validity days must be positive: " + value);
        }
        this.value = value;
    }

    public static PolicyValidityDays of(int value) {
        return new PolicyValidityDays(value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolicyValidityDays that = (PolicyValidityDays) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}