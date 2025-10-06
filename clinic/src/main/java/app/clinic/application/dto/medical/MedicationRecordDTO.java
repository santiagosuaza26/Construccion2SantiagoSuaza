package app.clinic.application.dto.medical;

/**
 * Data Transfer Object for medication information in medical records.
 * Contains medication details that can be associated with a medical record entry.
 */
public class MedicationRecordDTO {
    private String medicationName;
    private String dosage;
    private String treatmentDuration;
    private String instructions;

    // Default constructor
    public MedicationRecordDTO() {}

    // Constructor with parameters
    public MedicationRecordDTO(String medicationName, String dosage, String treatmentDuration, String instructions) {
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.treatmentDuration = treatmentDuration;
        this.instructions = instructions;
    }

    // Getters and Setters
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

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return String.format("MedicationRecordDTO{name='%s', dosage='%s', duration='%s'}",
                           medicationName, dosage, treatmentDuration);
    }
}