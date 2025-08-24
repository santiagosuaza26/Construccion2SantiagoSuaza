package app.application.dto;

public class InventoryItemResponse {
    private String id;
    private String name;
    private int quantity;

    public InventoryItemResponse(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
}
