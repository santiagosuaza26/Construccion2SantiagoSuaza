package app.application.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class VitalSignsRequest {
    
    @NotBlank(message = "Patient ID card is required")
    @Pattern(regexp = "\\d+", message = "Patient ID card must contain only numbers")
    private String patientIdCard;
    
    @NotBlank(message = "Nurse ID card is required")
    @Pattern(regexp = "\\d{1,10}", message = "Nurse ID card must be numeric with maximum 10 digits")
    private String nurseIdCard;
    
    @NotBlank(message = "Blood pressure is required")
    @Pattern(regexp = "\\d{2,3}/\\d{2,3}", message = "Blood pressure must be in format XXX/XXX (e.g., 120/80)")
    private String bloodPressure;
    
    @NotNull(message = "Temperature is required")
    @Min(value = 30, message = "Temperature must be at least 30°C")
    @Max(value = 45, message = "Temperature must be at most 45°C")
    private Double temperature;
    
    @NotNull(message = "Pulse is required")
    @Min(value = 30, message = "Pulse must be at least 30 bpm")
    @Max(value = 250, message = "Pulse must be at most 250 bpm")
    private Integer pulse;
    
    @NotNull(message = "Oxygen level is required")
    @Min(value = 0, message = "Oxygen level must be at least 0%")
    @Max(value = 100, message = "Oxygen level must be at most 100%")
    private Integer oxygenLevel;
    
    @NotBlank(message = "Visit date is required")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Visit date must be in DD/MM/YYYY format")
    private String visitDate;
    
    private String notes;
    
    private String relatedOrderNumber;
    
    // Default constructor
    public VitalSignsRequest() {}
    
    // Constructor with required fields
    public VitalSignsRequest(String patientIdCard, String nurseIdCard, String bloodPressure,
                           Double temperature, Integer pulse, Integer oxygenLevel, String visitDate) {
        this.patientIdCard = patientIdCard;
        this.nurseIdCard = nurseIdCard;
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.pulse = pulse;
        this.oxygenLevel = oxygenLevel;
        this.visitDate = visitDate;
    }
    
    // Constructor with all fields
    public VitalSignsRequest(String patientIdCard, String nurseIdCard, String bloodPressure,
                           Double temperature, Integer pulse, Integer oxygenLevel, String visitDate,
                           String notes, String relatedOrderNumber) {
        this.patientIdCard = patientIdCard;
        this.nurseIdCard = nurseIdCard;
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.pulse = pulse;
        this.oxygenLevel = oxygenLevel;
        this.visitDate = visitDate;
        this.notes = notes;
        this.relatedOrderNumber = relatedOrderNumber;
    }
    
    // Getters
    public String getPatientIdCard() { return patientIdCard; }
    public String getNurseIdCard() { return nurseIdCard; }
    public String getBloodPressure() { return bloodPressure; }
    public Double getTemperature() { return temperature; }
    public Integer getPulse() { return pulse; }
    public Integer getOxygenLevel() { return oxygenLevel; }
    public String getVisitDate() { return visitDate; }
    public String getNotes() { return notes; }
    public String getRelatedOrderNumber() { return relatedOrderNumber; }
    
    // Setters
    public void setPatientIdCard(String patientIdCard) { this.patientIdCard = patientIdCard; }
    public void setNurseIdCard(String nurseIdCard) { this.nurseIdCard = nurseIdCard; }
    public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public void setPulse(Integer pulse) { this.pulse = pulse; }
    public void setOxygenLevel(Integer oxygenLevel) { this.oxygenLevel = oxygenLevel; }
    public void setVisitDate(String visitDate) { this.visitDate = visitDate; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setRelatedOrderNumber(String relatedOrderNumber) { this.relatedOrderNumber = relatedOrderNumber; }
    
    // Utility methods
    public boolean hasRelatedOrder() {
        return relatedOrderNumber != null && !relatedOrderNumber.isBlank();
    }
    
    public boolean hasNotes() {
        return notes != null && !notes.isBlank();
    }
    
    @Override
    public String toString() {
        return "VitalSignsRequest{" +
                "patientIdCard='" + patientIdCard + '\'' +
                ", nurseIdCard='" + nurseIdCard + '\'' +
                ", bloodPressure='" + bloodPressure + '\'' +
                ", temperature=" + temperature +
                ", pulse=" + pulse +
                ", oxygenLevel=" + oxygenLevel +
                ", visitDate='" + visitDate + '\'' +
                ", hasNotes=" + hasNotes() +
                ", hasRelatedOrder=" + hasRelatedOrder() +
                '}';
    }
}