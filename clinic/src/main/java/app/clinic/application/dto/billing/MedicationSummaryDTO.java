package app.clinic.application.dto.billing;

/**
 * Data Transfer Object for medication summary information in billing.
 * Used for API responses containing medication summary data.
 */
public class MedicationSummaryDTO {
    private String medicationName;
    private String cost;
    private String dosage;

    // Default constructor
    public MedicationSummaryDTO() {}

    // Constructor with parameters
    public MedicationSummaryDTO(String medicationName, String cost, String dosage) {
        this.medicationName = medicationName;
        this.cost = cost;
        this.dosage = dosage;
    }

    // Getters and Setters
    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    @Override
    public String toString() {
        return String.format("MedicationSummaryDTO{medicationName='%s', cost='%s', dosage='%s'}",
                           medicationName, cost, dosage);
    }
}