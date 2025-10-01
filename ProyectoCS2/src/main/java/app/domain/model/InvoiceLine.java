package app.domain.model;

import java.math.BigDecimal;

public class InvoiceLine {
    private final String description;
    private final long amount;
    
    // Nuevos campos para compatibilidad con BillingService
    private BigDecimal unitPrice;
    private int quantity;

    public InvoiceLine(String description, long amount) {
        this.description = description;
        this.amount = amount;
        this.unitPrice = BigDecimal.valueOf(amount);
        this.quantity = 1;
        validate();
    }
    
    // Constructor adicional para BillingService
    public InvoiceLine(String description, BigDecimal unitPrice, int quantity) {
        this.description = description;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.amount = unitPrice.multiply(BigDecimal.valueOf(quantity)).longValue();
        validate();
    }

    public String getDescription() { return description; }
    public long getAmount() { return amount; }
    
    // Nuevos métodos para BillingService
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }

    private void validate() {
        if (description == null || description.isBlank()) throw new IllegalArgumentException("Description required");
        if (amount < 0) throw new IllegalArgumentException("Amount must be >= 0");
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Unit price must be >= 0");
        if (quantity < 0) throw new IllegalArgumentException("Quantity must be >= 0");
    }
}