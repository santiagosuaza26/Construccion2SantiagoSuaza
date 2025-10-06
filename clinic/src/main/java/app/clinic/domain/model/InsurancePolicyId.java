package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for insurance policies.
 */
public class InsurancePolicyId {
    private final String value;

    private InsurancePolicyId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Insurance policy ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static InsurancePolicyId of(String value) {
        return new InsurancePolicyId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsurancePolicyId that = (InsurancePolicyId) o;
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