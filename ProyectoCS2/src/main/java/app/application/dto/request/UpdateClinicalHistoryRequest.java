package app.application.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdateClinicalHistoryRequest {
    
    @NotBlank(message = "Patient ID card is required")
    @Pattern(regexp = "\\d+", message = "Patient ID card must contain only numbers")
    private String patientIdCard;
    
    @NotBlank(message = "Doctor ID card is required")
    @Pattern(regexp = "\\d{1,10}", message = "Doctor ID card must be numeric with maximum 10 digits")
    private String doctorIdCard;
    
    @NotBlank(message = "Entry date is required")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Entry date must be in DD/MM/YYYY format")
    private String entryDate;
    
    @NotBlank(message = "Reason for consultation is required")
    private String reason;
    
    private String symptoms;
    
    private String diagnosis;
    
    // Signos vitales (opcional, puede ser registrado por enfermera previamente)
    private VitalSignsData vitalSigns;
    
    // Números de órdenes relacionadas con esta entrada
    private List<String> relatedOrderNumbers;
    
    // Tipo de entrada en la historia clínica
    @Pattern(regexp = "CONSULTATION|FOLLOW_UP|EMERGENCY|DIAGNOSTIC_RESULTS|HOSPITALIZATION", 
                message = "Entry type must be: CONSULTATION, FOLLOW_UP, EMERGENCY, DIAGNOSTIC_RESULTS, or HOSPITALIZATION")
    private String entryType;
    
    // Default constructor
    public UpdateClinicalHistoryRequest() {}
    
    // Constructor básico
    public UpdateClinicalHistoryRequest(String patientIdCard, String doctorIdCard, String entryDate,
                                        String reason, String symptoms, String diagnosis) {
        this.patientIdCard = patientIdCard;
        this.doctorIdCard = doctorIdCard;
        this.entryDate = entryDate;
        this.reason = reason;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
    }
    
    // Constructor completo
    public UpdateClinicalHistoryRequest(String patientIdCard, String doctorIdCard, String entryDate,
                                        String reason, String symptoms, String diagnosis, 
                                        VitalSignsData vitalSigns, List<String> relatedOrderNumbers, 
                                        String entryType) {
        this.patientIdCard = patientIdCard;
        this.doctorIdCard = doctorIdCard;
        this.entryDate = entryDate;
        this.reason = reason;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.vitalSigns = vitalSigns;
        this.relatedOrderNumbers = relatedOrderNumbers;
        this.entryType = entryType;
    }
    
    // Getters
    public String getPatientIdCard() { return patientIdCard; }
    public String getDoctorIdCard() { return doctorIdCard; }
    public String getEntryDate() { return entryDate; }
    public String getReason() { return reason; }
    public String getSymptoms() { return symptoms; }
    public String getDiagnosis() { return diagnosis; }
    public VitalSignsData getVitalSigns() { return vitalSigns; }
    public List<String> getRelatedOrderNumbers() { return relatedOrderNumbers; }
    public String getEntryType() { return entryType; }
    
    // Setters
    public void setPatientIdCard(String patientIdCard) { this.patientIdCard = patientIdCard; }
    public void setDoctorIdCard(String doctorIdCard) { this.doctorIdCard = doctorIdCard; }
    public void setEntryDate(String entryDate) { this.entryDate = entryDate; }
    public void setReason(String reason) { this.reason = reason; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setVitalSigns(VitalSignsData vitalSigns) { this.vitalSigns = vitalSigns; }
    public void setRelatedOrderNumbers(List<String> relatedOrderNumbers) { this.relatedOrderNumbers = relatedOrderNumbers; }
    public void setEntryType(String entryType) { this.entryType = entryType; }
    
    // Utility methods
    public boolean hasVitalSigns() {
        return vitalSigns != null;
    }
    
    public boolean hasRelatedOrders() {
        return relatedOrderNumbers != null && !relatedOrderNumbers.isEmpty();
    }
    
    public boolean hasSymptoms() {
        return symptoms != null && !symptoms.isBlank();
    }
    
    public boolean hasDiagnosis() {
        return diagnosis != null && !diagnosis.isBlank();
    }
    
    public boolean isConsultation() {
        return "CONSULTATION".equals(entryType);
    }
    
    public boolean isFollowUp() {
        return "FOLLOW_UP".equals(entryType);
    }
    
    public boolean isEmergency() {
        return "EMERGENCY".equals(entryType);
    }
    
    public boolean isDiagnosticResults() {
        return "DIAGNOSTIC_RESULTS".equals(entryType);
    }
    
    public boolean isHospitalization() {
        return "HOSPITALIZATION".equals(entryType);
    }
    
    // Clase interna para signos vitales
    public static class VitalSignsData {
        private String bloodPressure;    // e.g., "120/80"
        private Double temperature;      // Celsius
        private Integer pulse;           // bpm
        private Integer oxygenLevel;     // %
        
        // Constructors
        public VitalSignsData() {}
        
        public VitalSignsData(String bloodPressure, Double temperature, Integer pulse, Integer oxygenLevel) {
            this.bloodPressure = bloodPressure;
            this.temperature = temperature;
            this.pulse = pulse;
            this.oxygenLevel = oxygenLevel;
        }
        
        // Getters and Setters
        public String getBloodPressure() { return bloodPressure; }
        public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }
        public Double getTemperature() { return temperature; }
        public void setTemperature(Double temperature) { this.temperature = temperature; }
        public Integer getPulse() { return pulse; }
        public void setPulse(Integer pulse) { this.pulse = pulse; }
        public Integer getOxygenLevel() { return oxygenLevel; }
        public void setOxygenLevel(Integer oxygenLevel) { this.oxygenLevel = oxygenLevel; }
        
        @Override
        public String toString() {
            return "VitalSignsData{" +
                    "bloodPressure='" + bloodPressure + '\'' +
                    ", temperature=" + temperature +
                    ", pulse=" + pulse +
                    ", oxygenLevel=" + oxygenLevel +
                    '}';
        }
    }
    
    @Override
    public String toString() {
        return "UpdateClinicalHistoryRequest{" +
                "patientIdCard='" + patientIdCard + '\'' +
                ", doctorIdCard='" + doctorIdCard + '\'' +
                ", entryDate='" + entryDate + '\'' +
                ", reason='" + reason + '\'' +
                ", entryType='" + entryType + '\'' +
                ", hasSymptoms=" + hasSymptoms() +
                ", hasDiagnosis=" + hasDiagnosis() +
                ", hasVitalSigns=" + hasVitalSigns() +
                ", hasRelatedOrders=" + hasRelatedOrders() +
                '}';
    }
}