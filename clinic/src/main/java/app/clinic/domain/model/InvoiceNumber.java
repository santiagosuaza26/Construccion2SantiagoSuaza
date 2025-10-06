package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing an invoice number.
 * Must be unique across all billing records.
 */
public class InvoiceNumber {
    private final String value;

    private InvoiceNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Invoice number cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static InvoiceNumber of(String value) {
        return new InvoiceNumber(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceNumber that = (InvoiceNumber) o;
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