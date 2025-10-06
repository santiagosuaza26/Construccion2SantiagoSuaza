package app.clinic.application.dto.order;

/**
 * Data Transfer Object for medication order information.
 * Used for API responses containing medication order data.
 */
public class MedicationOrderDTO {
    private String orderNumber;
    private Integer itemNumber;
    private String medicationName;
    private String dosage;
    private String treatmentDuration;
    private String cost;

    // Default constructor
    public MedicationOrderDTO() {}

    // Constructor with parameters
    public MedicationOrderDTO(String orderNumber, Integer itemNumber, String medicationName,
                             String dosage, String treatmentDuration, String cost) {
        this.orderNumber = orderNumber;
        this.itemNumber = itemNumber;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.treatmentDuration = treatmentDuration;
        this.cost = cost;
    }

    // Getters and Setters
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return String.format("MedicationOrderDTO{orderNumber='%s', itemNumber=%d, medicationName='%s'}",
                           orderNumber, itemNumber, medicationName);
    }
}