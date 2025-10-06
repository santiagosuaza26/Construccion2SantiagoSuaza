package app.clinic.application.dto.inventory;

/**
 * Data Transfer Object for inventory item information.
 * Used for API responses containing inventory item data.
 */
public class InventoryItemDTO {
    private String id;
    private String name;
    private String type;
    private String cost;
    private String description;
    private String dosage;
    private String frequency;
    private String duration;
    private boolean requiresSpecialistAssistance;
    private String specialistTypeId;
    private Integer currentStock;
    private Integer minimumStock;
    private Integer maximumStock;
    private boolean lowStock;

    // Default constructor
    public InventoryItemDTO() {}

    // Constructor with parameters
    public InventoryItemDTO(String id, String name, String type, String cost, String description,
                           String dosage, String frequency, String duration,
                           boolean requiresSpecialistAssistance, String specialistTypeId,
                           Integer currentStock, Integer minimumStock, Integer maximumStock, boolean lowStock) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.description = description;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.requiresSpecialistAssistance = requiresSpecialistAssistance;
        this.specialistTypeId = specialistTypeId;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        this.maximumStock = maximumStock;
        this.lowStock = lowStock;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isRequiresSpecialistAssistance() {
        return requiresSpecialistAssistance;
    }

    public void setRequiresSpecialistAssistance(boolean requiresSpecialistAssistance) {
        this.requiresSpecialistAssistance = requiresSpecialistAssistance;
    }

    public String getSpecialistTypeId() {
        return specialistTypeId;
    }

    public void setSpecialistTypeId(String specialistTypeId) {
        this.specialistTypeId = specialistTypeId;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(Integer minimumStock) {
        this.minimumStock = minimumStock;
    }

    public Integer getMaximumStock() {
        return maximumStock;
    }

    public void setMaximumStock(Integer maximumStock) {
        this.maximumStock = maximumStock;
    }

    public boolean isLowStock() {
        return lowStock;
    }

    public void setLowStock(boolean lowStock) {
        this.lowStock = lowStock;
    }

    @Override
    public String toString() {
        return String.format("InventoryItemDTO{id='%s', name='%s', type='%s', cost='%s', currentStock=%d}",
                           id, name, type, cost, currentStock);
    }
}