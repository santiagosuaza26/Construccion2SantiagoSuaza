package app.clinic.application.dto.medical;

import java.util.Map;

/**
 * Data Transfer Object for unstructured medical record data.
 * Used for API responses containing additional medical record information.
 */
public class MedicalRecordDataDTO {
    private Map<String, Object> additionalData;

    // Default constructor
    public MedicalRecordDataDTO() {}

    // Constructor with parameters
    public MedicalRecordDataDTO(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }

    // Getters and Setters
    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }

    @Override
    public String toString() {
        return String.format("MedicalRecordDataDTO{dataSize=%d}",
                           additionalData != null ? additionalData.size() : 0);
    }
}