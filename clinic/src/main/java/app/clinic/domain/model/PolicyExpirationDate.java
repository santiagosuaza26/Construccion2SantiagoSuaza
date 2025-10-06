package app.clinic.domain.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Value Object representing an insurance policy expiration date.
 * Validates that the expiration date is in the future.
 */
public class PolicyExpirationDate {
    private final LocalDate value;

    private PolicyExpirationDate(LocalDate value) {
        if (value == null) {
            throw new IllegalArgumentException("Policy expiration date cannot be null");
        }
        if (!isValidExpirationDate(value)) {
            throw new IllegalArgumentException("Policy expiration date must be in the future: " + value);
        }
        this.value = value;
    }

    public static PolicyExpirationDate of(LocalDate value) {
        return new PolicyExpirationDate(value);
    }

    public static PolicyExpirationDate of(int year, int month, int day) {
        return new PolicyExpirationDate(LocalDate.of(year, month, day));
    }

    private boolean isValidExpirationDate(LocalDate expirationDate) {
        return expirationDate.isAfter(LocalDate.now());
    }

    public LocalDate getValue() {
        return value;
    }

    public boolean isExpired() {
        return value.isBefore(LocalDate.now());
    }

    public long getDaysUntilExpiration() {
        return LocalDate.now().until(value).getDays();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolicyExpirationDate that = (PolicyExpirationDate) o;
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