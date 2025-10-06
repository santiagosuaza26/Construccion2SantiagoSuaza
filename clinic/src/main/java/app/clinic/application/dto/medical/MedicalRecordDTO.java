package app.clinic.application.dto.medical;

import java.util.Map;

/**
 * Data Transfer Object for complete patient medical record.
 * Used for API responses containing all medical record data.
 */
public class MedicalRecordDTO {
    private String patientCedula;
    private Map<String, MedicalRecordEntryDTO> records;

    // Default constructor
    public MedicalRecordDTO() {}

    // Constructor with parameters
    public MedicalRecordDTO(String patientCedula, Map<String, MedicalRecordEntryDTO> records) {
        this.patientCedula = patientCedula;
        this.records = records;
    }

    // Getters and Setters
    public String getPatientCedula() {
        return patientCedula;
    }

    public void setPatientCedula(String patientCedula) {
        this.patientCedula = patientCedula;
    }

    public Map<String, MedicalRecordEntryDTO> getRecords() {
        return records;
    }

    public void setRecords(Map<String, MedicalRecordEntryDTO> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return String.format("MedicalRecordDTO{patientCedula='%s', recordCount=%d}",
                           patientCedula, records != null ? records.size() : 0);
    }
}