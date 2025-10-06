package app.clinic.domain.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Value Object representing a billing date.
 */
public class BillingDate {
    private final LocalDate value;

    private BillingDate(LocalDate value) {
        this.value = value != null ? value : LocalDate.now();
    }

    public static BillingDate of(LocalDate value) {
        return new BillingDate(value);
    }

    public static BillingDate today() {
        return new BillingDate(LocalDate.now());
    }

    public LocalDate getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillingDate that = (BillingDate) o;
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