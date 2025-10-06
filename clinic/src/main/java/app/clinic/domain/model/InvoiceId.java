package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a unique identifier for invoices.
 */
public class InvoiceId {
    private final String value;

    private InvoiceId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Invoice ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static InvoiceId of(String value) {
        return new InvoiceId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceId invoiceId = (InvoiceId) o;
        return Objects.equals(value, invoiceId.value);
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