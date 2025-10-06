package app.clinic.application.dto.inventory;

import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for updating existing inventory items.
 * All fields are optional for partial updates.
 */
public class UpdateInventoryItemDTO {

    @Size(max = 100, message = "Item name must not exceed 100 characters")
    private String name;

    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    @Size(max = 50, message = "Dosage must not exceed 50 characters")
    private String dosage;

    @Size(max = 50, message = "Frequency must not exceed 50 characters")
    private String frequency;

    @Size(max = 50, message = "Duration must not exceed 50 characters")
    private String duration;

    private String cost;

    private Boolean requiresSpecialistAssistance;

    @Size(max = 20, message = "Specialist type ID must not exceed 20 characters")
    private String specialistTypeId;

    private String type;

    private Integer currentStock;

    private Integer minimumStock;

    private Integer maximumStock;

    // Default constructor
    public UpdateInventoryItemDTO() {}

    // Constructor with parameters
    public UpdateInventoryItemDTO(String name, String description, String dosage,
                                 String frequency, String duration, String cost,
                                 Boolean requiresSpecialistAssistance, String specialistTypeId,
                                 String type, Integer currentStock, Integer minimumStock, Integer maximumStock) {
        this.name = name;
        this.description = description;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.cost = cost;
        this.requiresSpecialistAssistance = requiresSpecialistAssistance;
        this.specialistTypeId = specialistTypeId;
        this.type = type;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        this.maximumStock = maximumStock;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Boolean getRequiresSpecialistAssistance() {
        return requiresSpecialistAssistance;
    }

    public void setRequiresSpecialistAssistance(Boolean requiresSpecialistAssistance) {
        this.requiresSpecialistAssistance = requiresSpecialistAssistance;
    }

    public String getSpecialistTypeId() {
        return specialistTypeId;
    }

    public void setSpecialistTypeId(String specialistTypeId) {
        this.specialistTypeId = specialistTypeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return String.format("UpdateInventoryItemDTO{name='%s', cost='%s', type='%s', currentStock=%d}",
                           name, cost, type, currentStock);
    }
}