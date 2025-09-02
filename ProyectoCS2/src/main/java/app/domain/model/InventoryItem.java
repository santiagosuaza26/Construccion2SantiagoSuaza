package app.domain.model;

public abstract class InventoryItem {

    private String id;
    private String name;
    private double cost;
    private int quantity;

    public InventoryItem(String id, String name, double cost, int quantity) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public int getQuantity() {
        return quantity;
    }
}

