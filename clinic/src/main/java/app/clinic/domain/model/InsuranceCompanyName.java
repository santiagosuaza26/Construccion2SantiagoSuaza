package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing an insurance company name.
 */
public class InsuranceCompanyName {
    private final String value;

    private InsuranceCompanyName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Insurance company name cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static InsuranceCompanyName of(String value) {
        return new InsuranceCompanyName(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsuranceCompanyName that = (InsuranceCompanyName) o;
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