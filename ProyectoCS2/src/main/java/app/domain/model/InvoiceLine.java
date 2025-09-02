package app.domain.model;

public class InvoiceLine {
    private final String description;
    private final long amount;

    public InvoiceLine(String description, long amount) {
        this.description = description;
        this.amount = amount;
        validate();
    }

    public String getDescription() { return description; }
    public long getAmount() { return amount; }

    private void validate() {
        if (description == null || description.isBlank()) throw new IllegalArgumentException("Description required");
        if (amount < 0) throw new IllegalArgumentException("Amount must be >= 0");
    }
}