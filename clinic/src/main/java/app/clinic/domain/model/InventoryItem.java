package app.clinic.domain.model;

import java.util.Objects;

/**
 * Entity representing an item in the clinic's inventory.
 * Can be medication, procedure, or diagnostic aid with associated cost.
 * Extended to include additional fields for better inventory management.
 */
public class InventoryItem {
    private final InventoryItemId id;
    private final InventoryItemName name;
    private final InventoryItemType type;
    private final Cost cost;
    private final boolean active;
    private final String description;
    private final Integer currentStock;
    private final Integer minimumStock;
    private final Integer maximumStock;

    private InventoryItem(InventoryItemId id, InventoryItemName name, InventoryItemType type,
                          Cost cost, boolean active, String description, Integer currentStock,
                          Integer minimumStock, Integer maximumStock) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.active = active;
        this.description = description != null ? description : "";
        this.currentStock = currentStock != null ? currentStock : 0;
        this.minimumStock = minimumStock != null ? minimumStock : 0;
        this.maximumStock = maximumStock != null ? maximumStock : 0;
    }

    public static InventoryItem of(InventoryItemId id, InventoryItemName name, InventoryItemType type,
                                   Cost cost) {
        return new InventoryItem(id, name, type, cost, true, "", 0, 0, 0);
    }

    public static InventoryItem of(InventoryItemId id, InventoryItemName name, InventoryItemType type,
                                   Cost cost, boolean active) {
        return new InventoryItem(id, name, type, cost, active, "", 0, 0, 0);
    }

    public static InventoryItem of(InventoryItemId id, InventoryItemName name, InventoryItemType type,
                                   Cost cost, boolean active, String description, Integer currentStock,
                                   Integer minimumStock, Integer maximumStock) {
        return new InventoryItem(id, name, type, cost, active, description, currentStock, minimumStock, maximumStock);
    }

    public InventoryItemId getId() {
        return id;
    }

    public InventoryItemName getName() {
        return name;
    }

    public InventoryItemType getType() {
        return type;
    }

    public Cost getCost() {
        return cost;
    }

    public boolean isActive() {
        return active;
    }

    public String getDescription() {
        return description;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public Integer getMinimumStock() {
        return minimumStock;
    }

    public Integer getMaximumStock() {
        return maximumStock;
    }

    public boolean isLowStock() {
        return currentStock <= minimumStock;
    }

    public boolean isOverStock() {
        return currentStock > maximumStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryItem that = (InventoryItem) o;
        return active == that.active &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                type == that.type &&
                Objects.equals(cost, that.cost) &&
                Objects.equals(description, that.description) &&
                Objects.equals(currentStock, that.currentStock) &&
                Objects.equals(minimumStock, that.minimumStock) &&
                Objects.equals(maximumStock, that.maximumStock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, cost, active, description, currentStock, minimumStock, maximumStock);
    }

    @Override
    public String toString() {
        return String.format("InventoryItem{id=%s, name=%s, type=%s, cost=%s, active=%s, description='%s', currentStock=%d, minimumStock=%d, maximumStock=%d}",
                           id, name, type, cost, active, description, currentStock, minimumStock, maximumStock);
    }
}