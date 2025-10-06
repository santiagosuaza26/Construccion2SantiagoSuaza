package app.clinic.application.dto.inventory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for creating new inventory items.
 * Contains validation annotations for input data.
 */
public class CreateInventoryItemDTO {

    @NotBlank(message = "Item name is required")
    @Size(max = 100, message = "Item name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Item type is required")
    @Pattern(regexp = "^(MEDICAMENTO|PROCEDIMIENTO|AYUDA_DIAGNOSTICA)$",
             message = "Item type must be MEDICAMENTO, PROCEDIMIENTO, or AYUDA_DIAGNOSTICA")
    private String type;

    @NotNull(message = "Cost is required")
    private String cost;

    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    @Size(max = 50, message = "Dosage must not exceed 50 characters")
    private String dosage;

    @Size(max = 50, message = "Frequency must not exceed 50 characters")
    private String frequency;

    @Size(max = 50, message = "Duration must not exceed 50 characters")
    private String duration;

    @NotNull(message = "Requires specialist assistance is required")
    private Boolean requiresSpecialistAssistance;

    @Size(max = 20, message = "Specialist type ID must not exceed 20 characters")
    private String specialistTypeId;

    // Default constructor
    public CreateInventoryItemDTO() {}

    // Constructor with parameters
    public CreateInventoryItemDTO(String name, String type, String cost, String description,
                                 String dosage, String frequency, String duration,
                                 Boolean requiresSpecialistAssistance, String specialistTypeId) {
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.description = description;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.requiresSpecialistAssistance = requiresSpecialistAssistance;
        this.specialistTypeId = specialistTypeId;
    }

    // Getters and Setters
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
        return String.format("CreateInventoryItemDTO{name='%s', type='%s', cost='%s'}",
                           name, type, cost);
    }
}