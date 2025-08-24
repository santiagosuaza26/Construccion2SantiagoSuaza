package app.domain.model;

public abstract class InventoryItem {

    private String id;
    private String name;
    private double cost;

    public InventoryItem(String id, String name, double cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

