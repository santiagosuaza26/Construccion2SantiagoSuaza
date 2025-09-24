package app.application.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateOrderRequest {
    
    // Información básica de la orden
    @NotBlank(message = "Patient ID card is required")
    @Pattern(regexp = "\\d+", message = "Patient ID card must contain only numbers")
    private String patientIdCard;
    
    @NotBlank(message = "Doctor ID card is required")
    @Pattern(regexp = "\\d{1,10}", message = "Doctor ID card must be numeric with maximum 10 digits")
    private String doctorIdCard;
    
    @NotBlank(message = "Order date is required")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Order date must be in DD/MM/YYYY format")
    private String orderDate;
    
    // Información clínica (para historia clínica)
    @NotBlank(message = "Reason for consultation is required")
    private String reason;
    
    private String symptoms;
    
    private String diagnosis;
    
    // Signos vitales (opcional, registrado por enfermera)
    private VitalSignsRequest vitalSigns;
    
    // Items de la orden (solo uno de estos tipos por orden según reglas del documento)
    @Valid
    private List<MedicationItemRequest> medications;
    
    @Valid
    private List<ProcedureItemRequest> procedures;
    
    @Valid
    private List<DiagnosticItemRequest> diagnostics;
    
    // Default constructor
    public CreateOrderRequest() {}
    
    // Constructor básico
    public CreateOrderRequest(String patientIdCard, String doctorIdCard, String orderDate, 
                            String reason, String symptoms, String diagnosis) {
        this.patientIdCard = patientIdCard;
        this.doctorIdCard = doctorIdCard;
        this.orderDate = orderDate;
        this.reason = reason;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
    }
    
    // Getters
    public String getPatientIdCard() { return patientIdCard; }
    public String getDoctorIdCard() { return doctorIdCard; }
    public String getOrderDate() { return orderDate; }
    public String getReason() { return reason; }
    public String getSymptoms() { return symptoms; }
    public String getDiagnosis() { return diagnosis; }
    public VitalSignsRequest getVitalSigns() { return vitalSigns; }
    public List<MedicationItemRequest> getMedications() { return medications; }
    public List<ProcedureItemRequest> getProcedures() { return procedures; }
    public List<DiagnosticItemRequest> getDiagnostics() { return diagnostics; }
    
    // Setters
    public void setPatientIdCard(String patientIdCard) { this.patientIdCard = patientIdCard; }
    public void setDoctorIdCard(String doctorIdCard) { this.doctorIdCard = doctorIdCard; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public void setReason(String reason) { this.reason = reason; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setVitalSigns(VitalSignsRequest vitalSigns) { this.vitalSigns = vitalSigns; }
    public void setMedications(List<MedicationItemRequest> medications) { this.medications = medications; }
    public void setProcedures(List<ProcedureItemRequest> procedures) { this.procedures = procedures; }
    public void setDiagnostics(List<DiagnosticItemRequest> diagnostics) { this.diagnostics = diagnostics; }
    
    // Métodos de utilidad para validar reglas de negocio
    public boolean hasMedications() {
        return medications != null && !medications.isEmpty();
    }
    
    public boolean hasProcedures() {
        return procedures != null && !procedures.isEmpty();
    }
    
    public boolean hasDiagnostics() {
        return diagnostics != null && !diagnostics.isEmpty();
    }
    
    public boolean hasMultipleItemTypes() {
        int typesCount = 0;
        if (hasMedications()) typesCount++;
        if (hasProcedures()) typesCount++;
        if (hasDiagnostics()) typesCount++;
        return typesCount > 1;
    }
    
    public String getOrderType() {
        if (hasMedications()) return "MEDICATION";
        if (hasProcedures()) return "PROCEDURE";
        if (hasDiagnostics()) return "DIAGNOSTIC";
        return "EMPTY";
    }
    
    // Clases internas para los items
    public static class MedicationItemRequest {
        @NotBlank(message = "Medication ID is required")
        private String medicationId;
        
        @NotBlank(message = "Medication name is required")
        private String medicationName;
        
        private String dosage;
        private String treatmentDuration;
        private Long cost;
        
        // Constructors
        public MedicationItemRequest() {}
        
        public MedicationItemRequest(String medicationId, String medicationName, 
                                    String dosage, String treatmentDuration, Long cost) {
            this.medicationId = medicationId;
            this.medicationName = medicationName;
            this.dosage = dosage;
            this.treatmentDuration = treatmentDuration;
            this.cost = cost;
        }
        
        // Getters and Setters
        public String getMedicationId() { return medicationId; }
        public void setMedicationId(String medicationId) { this.medicationId = medicationId; }
        public String getMedicationName() { return medicationName; }
        public void setMedicationName(String medicationName) { this.medicationName = medicationName; }
        public String getDosage() { return dosage; }
        public void setDosage(String dosage) { this.dosage = dosage; }
        public String getTreatmentDuration() { return treatmentDuration; }
        public void setTreatmentDuration(String treatmentDuration) { this.treatmentDuration = treatmentDuration; }
        public Long getCost() { return cost; }
        public void setCost(Long cost) { this.cost = cost; }
    }
    
    public static class ProcedureItemRequest {
        @NotBlank(message = "Procedure ID is required")
        private String procedureId;
        
        @NotBlank(message = "Procedure name is required")
        private String procedureName;
        
        private Integer quantity;
        private String frequency;
        private Boolean specialistRequired;
        private String specialtyId;
        private Long cost;
        
        // Constructors
        public ProcedureItemRequest() {}
        
        public ProcedureItemRequest(String procedureId, String procedureName, Integer quantity, 
                                    String frequency, Boolean specialistRequired, String specialtyId, Long cost) {
            this.procedureId = procedureId;
            this.procedureName = procedureName;
            this.quantity = quantity;
            this.frequency = frequency;
            this.specialistRequired = specialistRequired;
            this.specialtyId = specialtyId;
            this.cost = cost;
        }
        
        // Getters and Setters
        public String getProcedureId() { return procedureId; }
        public void setProcedureId(String procedureId) { this.procedureId = procedureId; }
        public String getProcedureName() { return procedureName; }
        public void setProcedureName(String procedureName) { this.procedureName = procedureName; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public String getFrequency() { return frequency; }
        public void setFrequency(String frequency) { this.frequency = frequency; }
        public Boolean getSpecialistRequired() { return specialistRequired; }
        public void setSpecialistRequired(Boolean specialistRequired) { this.specialistRequired = specialistRequired; }
        public String getSpecialtyId() { return specialtyId; }
        public void setSpecialtyId(String specialtyId) { this.specialtyId = specialtyId; }
        public Long getCost() { return cost; }
        public void setCost(Long cost) { this.cost = cost; }
    }
    
    public static class DiagnosticItemRequest {
        @NotBlank(message = "Diagnostic ID is required")
        private String diagnosticId;
        
        @NotBlank(message = "Diagnostic name is required")
        private String diagnosticName;
        
        private Integer quantity;
        private Boolean specialistRequired;
        private String specialtyId;
        private Long cost;
        
        // Constructors
        public DiagnosticItemRequest() {}
        
        public DiagnosticItemRequest(String diagnosticId, String diagnosticName, Integer quantity,
                                    Boolean specialistRequired, String specialtyId, Long cost) {
            this.diagnosticId = diagnosticId;
            this.diagnosticName = diagnosticName;
            this.quantity = quantity;
            this.specialistRequired = specialistRequired;
            this.specialtyId = specialtyId;
            this.cost = cost;
        }
        
        // Getters and Setters
        public String getDiagnosticId() { return diagnosticId; }
        public void setDiagnosticId(String diagnosticId) { this.diagnosticId = diagnosticId; }
        public String getDiagnosticName() { return diagnosticName; }
        public void setDiagnosticName(String diagnosticName) { this.diagnosticName = diagnosticName; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public Boolean getSpecialistRequired() { return specialistRequired; }
        public void setSpecialistRequired(Boolean specialistRequired) { this.specialistRequired = specialistRequired; }
        public String getSpecialtyId() { return specialtyId; }
        public void setSpecialtyId(String specialtyId) { this.specialtyId = specialtyId; }
        public Long getCost() { return cost; }
        public void setCost(Long cost) { this.cost = cost; }
    }
    
    public static class VitalSignsRequest {
        private String bloodPressure;    // e.g., "120/80"
        private Double temperature;      // Celsius
        private Integer pulse;           // bpm
        private Integer oxygenLevel;     // %
        
        // Constructors
        public VitalSignsRequest() {}
        
        public VitalSignsRequest(String bloodPressure, Double temperature, Integer pulse, Integer oxygenLevel) {
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
    }
    
    @Override
    public String toString() {
        return "CreateOrderRequest{" +
                "patientIdCard='" + patientIdCard + '\'' +
                ", doctorIdCard='" + doctorIdCard + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", reason='" + reason + '\'' +
                ", symptoms='" + symptoms + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", orderType='" + getOrderType() + '\'' +
                ", hasVitalSigns=" + (vitalSigns != null) +
                '}';
    }
}