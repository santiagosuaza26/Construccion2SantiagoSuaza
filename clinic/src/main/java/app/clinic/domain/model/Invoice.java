package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing an invoice for medical services.
 * Contains invoice number, billing details, and total amount.
 */
public class Invoice {
    private final InvoiceNumber invoiceNumber;
    private final BillingDetails billingDetails;
    private final Money totalAmount;
    private final BillingDate billingDate;

    private Invoice(InvoiceNumber invoiceNumber, BillingDetails billingDetails,
                   Money totalAmount, BillingDate billingDate) {
        this.invoiceNumber = invoiceNumber;
        this.billingDetails = billingDetails;
        this.totalAmount = totalAmount;
        this.billingDate = billingDate;
    }

    public static Invoice of(InvoiceNumber invoiceNumber, BillingDetails billingDetails,
                            Money totalAmount, BillingDate billingDate) {
        return new Invoice(invoiceNumber, billingDetails, totalAmount, billingDate);
    }

    public InvoiceNumber getInvoiceNumber() {
        return invoiceNumber;
    }

    public BillingDetails getBillingDetails() {
        return billingDetails;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public BillingDate getBillingDate() {
        return billingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(invoiceNumber, invoice.invoiceNumber) &&
               Objects.equals(billingDetails, invoice.billingDetails) &&
               Objects.equals(totalAmount, invoice.totalAmount) &&
               Objects.equals(billingDate, invoice.billingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceNumber, billingDetails, totalAmount, billingDate);
    }

    @Override
    public String toString() {
        return String.format("Invoice{number=%s, total=%s, date=%s}",
                           invoiceNumber, totalAmount, billingDate);
    }
}