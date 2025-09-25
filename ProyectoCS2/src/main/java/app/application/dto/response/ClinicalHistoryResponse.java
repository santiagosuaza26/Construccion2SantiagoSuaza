package app.application.dto.response;

import java.util.List;

public class ClinicalHistoryResponse {
    
    private String patientIdCard;
    private String patientName;
    private int patientAge;
    private String patientGender;
    
    // Lista de entradas en la historia clínica
    private List<ClinicalHistoryEntryInfo> entries;
    
    // Resumen estadístico
    private int totalEntries;
    private String firstConsultationDate;
    private String lastConsultationDate;
    private int consultationsThisYear;
    
    // Default constructor
    public ClinicalHistoryResponse() {}
    
    // Constructor básico
    public ClinicalHistoryResponse(String patientIdCard, String patientName, int patientAge, String patientGender) {
        this.patientIdCard = patientIdCard;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
    }
    
    // Constructor completo
    public ClinicalHistoryResponse(String patientIdCard, String patientName, int patientAge, String patientGender,
                                  List<ClinicalHistoryEntryInfo> entries, int totalEntries, 
                                  String firstConsultationDate, String lastConsultationDate, int consultationsThisYear) {
        this.patientIdCard = patientIdCard;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
        this.entries = entries;
        this.totalEntries = totalEntries;
        this.firstConsultationDate = firstConsultationDate;
        this.lastConsultationDate = lastConsultationDate;
        this.consultationsThisYear = consultationsThisYear;
    }
    
    // Getters
    public String getPatientIdCard() { return patientIdCard; }
    public String getPatientName() { return patientName; }
    public int getPatientAge() { return patientAge; }
    public String getPatientGender() { return patientGender; }
    public List<ClinicalHistoryEntryInfo> getEntries() { return entries; }
    public int getTotalEntries() { return totalEntries; }
    public String getFirstConsultationDate() { return firstConsultationDate; }
    public String getLastConsultationDate() { return lastConsultationDate; }
    public int getConsultationsThisYear() { return consultationsThisYear; }
    
    // Setters
    public void setPatientIdCard(String patientIdCard) { this.patientIdCard = patientIdCard; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setPatientAge(int patientAge) { this.patientAge = patientAge; }
    public void setPatientGender(String patientGender) { this.patientGender = patientGender; }
    public void setEntries(List<ClinicalHistoryEntryInfo> entries) { this.entries = entries; }
    public void setTotalEntries(int totalEntries) { this.totalEntries = totalEntries; }
    public void setFirstConsultationDate(String firstConsultationDate) { this.firstConsultationDate = firstConsultationDate; }
    public void setLastConsultationDate(String lastConsultationDate) { this.lastConsultationDate = lastConsultationDate; }
    public void setConsultationsThisYear(int consultationsThisYear) { this.consultationsThisYear = consultationsThisYear; }
    
    // Utility methods
    public boolean hasEntries() {
        return entries != null && !entries.isEmpty();
    }
    
    public boolean isNewPatient() {
        return totalEntries <= 1;
    }
    
    public boolean isFrequentPatient() {
        return consultationsThisYear >= 5;
    }
    
    public int getEntriesCount() {
        return entries != null ? entries.size() : 0;
    }
    
    // Clase interna para entradas de historia clínica
    public static class ClinicalHistoryEntryInfo {
        private String entryDate;
        private String doctorIdCard;
        private String doctorName;
        private String entryType;
        private String reason;
        private String symptoms;
        private String diagnosis;
        private VitalSignsInfo vitalSigns;
        private List<String> relatedOrderNumbers;
        private String notes;
        
        public ClinicalHistoryEntryInfo() {}
        
        public ClinicalHistoryEntryInfo(String entryDate, String doctorIdCard, String doctorName,
                                       String entryType, String reason, String symptoms, String diagnosis) {
            this.entryDate = entryDate;
            this.doctorIdCard = doctorIdCard;
            this.doctorName = doctorName;
            this.entryType = entryType;
            this.reason = reason;
            this.symptoms = symptoms;
            this.diagnosis = diagnosis;
        }
        
        // Constructor completo
        public ClinicalHistoryEntryInfo(String entryDate, String doctorIdCard, String doctorName,
                                       String entryType, String reason, String symptoms, String diagnosis,
                                       VitalSignsInfo vitalSigns, List<String> relatedOrderNumbers, String notes) {
            this.entryDate = entryDate;
            this.doctorIdCard = doctorIdCard;
            this.doctorName = doctorName;
            this.entryType = entryType;
            this.reason = reason;
            this.symptoms = symptoms;
            this.diagnosis = diagnosis;
            this.vitalSigns = vitalSigns;
            this.relatedOrderNumbers = relatedOrderNumbers;
            this.notes = notes;
        }
        
        // Getters and Setters
        public String getEntryDate() { return entryDate; }
        public void setEntryDate(String entryDate) { this.entryDate = entryDate; }
        public String getDoctorIdCard() { return doctorIdCard; }
        public void setDoctorIdCard(String doctorIdCard) { this.doctorIdCard = doctorIdCard; }
        public String getDoctorName() { return doctorName; }
        public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
        public String getEntryType() { return entryType; }
        public void setEntryType(String entryType) { this.entryType = entryType; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public String getSymptoms() { return symptoms; }
        public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
        public String getDiagnosis() { return diagnosis; }
        public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
        public VitalSignsInfo getVitalSigns() { return vitalSigns; }
        public void setVitalSigns(VitalSignsInfo vitalSigns) { this.vitalSigns = vitalSigns; }
        public List<String> getRelatedOrderNumbers() { return relatedOrderNumbers; }
        public void setRelatedOrderNumbers(List<String> relatedOrderNumbers) { this.relatedOrderNumbers = relatedOrderNumbers; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        
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
        
        public boolean hasNotes() {
            return notes != null && !notes.isBlank();
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
        
        public int getRelatedOrdersCount() {
            return relatedOrderNumbers != null ? relatedOrderNumbers.size() : 0;
        }
        
        @Override
        public String toString() {
            return "ClinicalHistoryEntryInfo{" +
                    "entryDate='" + entryDate + '\'' +
                    ", doctorName='" + doctorName + '\'' +
                    ", entryType='" + entryType + '\'' +
                    ", reason='" + reason + '\'' +
                    ", hasDiagnosis=" + hasDiagnosis() +
                    ", hasVitalSigns=" + hasVitalSigns() +
                    ", relatedOrdersCount=" + getRelatedOrdersCount() +
                    '}';
        }
    }
    
    // Clase interna para signos vitales (reutilizada de OrderResponse)
    public static class VitalSignsInfo {
        private String bloodPressure;
        private double temperature;
        private int pulse;
        private int oxygenLevel;
        private String recordedBy;
        private String recordedDate;
        
        public VitalSignsInfo() {}
        
        public VitalSignsInfo(String bloodPressure, double temperature, int pulse, int oxygenLevel,
                            String recordedBy, String recordedDate) {
            this.bloodPressure = bloodPressure;
            this.temperature = temperature;
            this.pulse = pulse;
            this.oxygenLevel = oxygenLevel;
            this.recordedBy = recordedBy;
            this.recordedDate = recordedDate;
        }
        
        // Getters and Setters
        public String getBloodPressure() { return bloodPressure; }
        public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }
        public double getTemperature() { return temperature; }
        public void setTemperature(double temperature) { this.temperature = temperature; }
        public int getPulse() { return pulse; }
        public void setPulse(int pulse) { this.pulse = pulse; }
        public int getOxygenLevel() { return oxygenLevel; }
        public void setOxygenLevel(int oxygenLevel) { this.oxygenLevel = oxygenLevel; }
        public String getRecordedBy() { return recordedBy; }
        public void setRecordedBy(String recordedBy) { this.recordedBy = recordedBy; }
        public String getRecordedDate() { return recordedDate; }
        public void setRecordedDate(String recordedDate) { this.recordedDate = recordedDate; }
        
        @Override
        public String toString() {
            return "VitalSignsInfo{" +
                    "bloodPressure='" + bloodPressure + '\'' +
                    ", temperature=" + temperature +
                    ", pulse=" + pulse +
                    ", oxygenLevel=" + oxygenLevel +
                    ", recordedBy='" + recordedBy + '\'' +
                    ", recordedDate='" + recordedDate + '\'' +
                    '}';
        }
    }
    
    @Override
    public String toString() {
        return "ClinicalHistoryResponse{" +
                "patientIdCard='" + patientIdCard + '\'' +
                ", patientName='" + patientName + '\'' +
                ", patientAge=" + patientAge +
                ", patientGender='" + patientGender + '\'' +
                ", totalEntries=" + totalEntries +
                ", consultationsThisYear=" + consultationsThisYear +
                ", firstConsultationDate='" + firstConsultationDate + '\'' +
                ", lastConsultationDate='" + lastConsultationDate + '\'' +
                ", isNewPatient=" + isNewPatient() +
                ", isFrequentPatient=" + isFrequentPatient() +
                '}';
    }
}