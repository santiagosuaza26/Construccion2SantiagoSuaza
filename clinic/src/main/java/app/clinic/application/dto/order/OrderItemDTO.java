package app.clinic.application.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for order item information.
 * Represents individual items within a medical order.
 */
public class OrderItemDTO {

    @NotBlank(message = "Item type is required")
    @Pattern(regexp = "^(MEDICATION|PROCEDURE|DIAGNOSTIC_AID)$",
             message = "Item type must be MEDICATION, PROCEDURE, or DIAGNOSTIC_AID")
    private String type;

    @NotBlank(message = "Inventory item ID is required")
    @Size(max = 20, message = "Inventory item ID must not exceed 20 characters")
    private String inventoryItemId;

    @NotBlank(message = "Item name is required")
    @Size(max = 100, message = "Item name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @Size(max = 50, message = "Dosage must not exceed 50 characters")
    private String dosage;

    @Size(max = 50, message = "Treatment duration must not exceed 50 characters")
    private String treatmentDuration;

    @Size(max = 50, message = "Frequency must not exceed 50 characters")
    private String frequency;

    @NotNull(message = "Cost is required")
    private String cost;

    @NotNull(message = "Requires specialist assistance is required")
    private Boolean requiresSpecialistAssistance;

    @Size(max = 20, message = "Specialist type ID must not exceed 20 characters")
    private String specialistTypeId;

    // Default constructor
    public OrderItemDTO() {}

    // Constructor with parameters
    public OrderItemDTO(String type, String inventoryItemId, String name, Integer quantity,
                       String dosage, String treatmentDuration, String frequency, String cost,
                       Boolean requiresSpecialistAssistance, String specialistTypeId) {
        this.type = type;
        this.inventoryItemId = inventoryItemId;
        this.name = name;
        this.quantity = quantity;
        this.dosage = dosage;
        this.treatmentDuration = treatmentDuration;
        this.frequency = frequency;
        this.cost = cost;
        this.requiresSpecialistAssistance = requiresSpecialistAssistance;
        this.specialistTypeId = specialistTypeId;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(String inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getTreatmentDuration() {
        return treatmentDuration;
    }

    public void setTreatmentDuration(String treatmentDuration) {
        this.treatmentDuration = treatmentDuration;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
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

    @Override
    public String toString() {
        return String.format("OrderItemDTO{type='%s', name='%s', quantity=%d}",
                           type, name, quantity);
    }
}