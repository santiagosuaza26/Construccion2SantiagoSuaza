package app.domain.model;

public class Medication {
    private final String medicationId;
    private final String name;
    private final String description;
    private final int stock;
    private final long price; // in pesos

    public Medication(String medicationId, String name, String description, int stock, long price) {
        this.medicationId = medicationId;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        validate();
    }

    public String getMedicationId() { return medicationId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getStock() { return stock; }
    public long getPrice() { return price; }

    private void validate() {
        if (medicationId == null || medicationId.isBlank()) throw new IllegalArgumentException("Medication id required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Medication name required");
        if (stock < 0 || price < 0) throw new IllegalArgumentException("Invalid stock/price");
    }
}
