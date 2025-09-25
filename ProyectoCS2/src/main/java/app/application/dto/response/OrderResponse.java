package app.application.dto.response;

import java.util.List;

public class OrderResponse {
    
    private String orderNumber;
    private String patientIdCard;
    private String patientName;
    private String doctorIdCard;
    private String doctorName;
    private String creationDate;
    private String orderType;
    private String status;
    
    // Información clínica
    private String reason;
    private String symptoms;
    private String diagnosis;
    
    // Items de la orden
    private List<OrderItemInfo> items;
    
    // Signos vitales (si fueron registrados)
    private VitalSignsInfo vitalSigns;
    
    // Totales y costos
    private long totalCost;
    private int itemCount;
    
    // Default constructor
    public OrderResponse() {}
    
    // Constructor básico
    public OrderResponse(String orderNumber, String patientIdCard, String patientName,
                        String doctorIdCard, String doctorName, String creationDate, String orderType) {
        this.orderNumber = orderNumber;
        this.patientIdCard = patientIdCard;
        this.patientName = patientName;
        this.doctorIdCard = doctorIdCard;
        this.doctorName = doctorName;
        this.creationDate = creationDate;
        this.orderType = orderType;
    }
    
    // Constructor completo
    public OrderResponse(String orderNumber, String patientIdCard, String patientName,
                        String doctorIdCard, String doctorName, String creationDate, String orderType,
                        String status, String reason, String symptoms, String diagnosis,
                        List<OrderItemInfo> items, VitalSignsInfo vitalSigns, long totalCost, int itemCount) {
        this.orderNumber = orderNumber;
        this.patientIdCard = patientIdCard;
        this.patientName = patientName;
        this.doctorIdCard = doctorIdCard;
        this.doctorName = doctorName;
        this.creationDate = creationDate;
        this.orderType = orderType;
        this.status = status;
        this.reason = reason;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.items = items;
        this.vitalSigns = vitalSigns;
        this.totalCost = totalCost;
        this.itemCount = itemCount;
    }
    
    // Getters
    public String getOrderNumber() { return orderNumber; }
    public String getPatientIdCard() { return patientIdCard; }
    public String getPatientName() { return patientName; }
    public String getDoctorIdCard() { return doctorIdCard; }
    public String getDoctorName() { return doctorName; }
    public String getCreationDate() { return creationDate; }
    public String getOrderType() { return orderType; }
    public String getStatus() { return status; }
    public String getReason() { return reason; }
    public String getSymptoms() { return symptoms; }
    public String getDiagnosis() { return diagnosis; }
    public List<OrderItemInfo> getItems() { return items; }
    public VitalSignsInfo getVitalSigns() { return vitalSigns; }
    public long getTotalCost() { return totalCost; }
    public int getItemCount() { return itemCount; }
    
    // Setters
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public void setPatientIdCard(String patientIdCard) { this.patientIdCard = patientIdCard; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setDoctorIdCard(String doctorIdCard) { this.doctorIdCard = doctorIdCard; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
    public void setStatus(String status) { this.status = status; }
    public void setReason(String reason) { this.reason = reason; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setItems(List<OrderItemInfo> items) { this.items = items; }
    public void setVitalSigns(VitalSignsInfo vitalSigns) { this.vitalSigns = vitalSigns; }
    public void setTotalCost(long totalCost) { this.totalCost = totalCost; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }
    
    // Utility methods
    public boolean isMedicationOrder() {
        return "MEDICATION".equals(orderType);
    }
    
    public boolean isProcedureOrder() {
        return "PROCEDURE".equals(orderType);
    }
    
    public boolean isDiagnosticOrder() {
        return "DIAGNOSTIC".equals(orderType);
    }
    
    public boolean hasItems() {
        return items != null && !items.isEmpty();
    }
    
    public boolean hasVitalSigns() {
        return vitalSigns != null;
    }
    
    public boolean hasSymptoms() {
        return symptoms != null && !symptoms.isBlank();
    }
    
    public boolean hasDiagnosis() {
        return diagnosis != null && !diagnosis.isBlank();
    }
    
    public boolean isPending() {
        return "PENDING".equals(status);
    }
    
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
    
    public String getFormattedTotalCost() {
        return String.format("$%,d", totalCost);
    }
    
    // Clases internas para información de items
    public static class OrderItemInfo {
        private int itemNumber;
        private String itemType;
        private String itemId;
        private String itemName;
        private String description;
        private int quantity;
        private String dosage;
        private String frequency;
        private String duration;
        private boolean specialistRequired;
        private String specialtyId;
        private String specialtyName;
        private long cost;
        
        // Constructor básico
        public OrderItemInfo() {}
        
        // Constructor para medicamento
        public OrderItemInfo(int itemNumber, String itemId, String itemName, String dosage, 
                           String duration, long cost) {
            this.itemNumber = itemNumber;
            this.itemType = "MEDICATION";
            this.itemId = itemId;
            this.itemName = itemName;
            this.dosage = dosage;
            this.duration = duration;
            this.cost = cost;
        }
        
        // Constructor para procedimiento
        public OrderItemInfo(int itemNumber, String itemId, String itemName, int quantity, 
                           String frequency, boolean specialistRequired, String specialtyName, long cost) {
            this.itemNumber = itemNumber;
            this.itemType = "PROCEDURE";
            this.itemId = itemId;
            this.itemName = itemName;
            this.quantity = quantity;
            this.frequency = frequency;
            this.specialistRequired = specialistRequired;
            this.specialtyName = specialtyName;
            this.cost = cost;
        }
        
        // Getters and Setters
        public int getItemNumber() { return itemNumber; }
        public void setItemNumber(int itemNumber) { this.itemNumber = itemNumber; }
        public String getItemType() { return itemType; }
        public void setItemType(String itemType) { this.itemType = itemType; }
        public String getItemId() { return itemId; }
        public void setItemId(String itemId) { this.itemId = itemId; }
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public String getDosage() { return dosage; }
        public void setDosage(String dosage) { this.dosage = dosage; }
        public String getFrequency() { return frequency; }
        public void setFrequency(String frequency) { this.frequency = frequency; }
        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }
        public boolean isSpecialistRequired() { return specialistRequired; }
        public void setSpecialistRequired(boolean specialistRequired) { this.specialistRequired = specialistRequired; }
        public String getSpecialtyId() { return specialtyId; }
        public void setSpecialtyId(String specialtyId) { this.specialtyId = specialtyId; }
        public String getSpecialtyName() { return specialtyName; }
        public void setSpecialtyName(String specialtyName) { this.specialtyName = specialtyName; }
        public long getCost() { return cost; }
        public void setCost(long cost) { this.cost = cost; }
        
        public String getFormattedCost() {
            return String.format("$%,d", cost);
        }
        
        @Override
        public String toString() {
            return "OrderItemInfo{" +
                    "itemNumber=" + itemNumber +
                    ", itemType='" + itemType + '\'' +
                    ", itemName='" + itemName + '\'' +
                    ", cost=" + cost +
                    ", specialistRequired=" + specialistRequired +
                    '}';
        }
    }
    
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
        return "OrderResponse{" +
                "orderNumber='" + orderNumber + '\'' +
                ", patientName='" + patientName + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", orderType='" + orderType + '\'' +
                ", status='" + status + '\'' +
                ", itemCount=" + itemCount +
                ", totalCost=" + getFormattedTotalCost() +
                ", hasVitalSigns=" + hasVitalSigns() +
                '}';
    }
}