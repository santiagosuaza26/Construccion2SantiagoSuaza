package app.application.dto.response;

import java.util.List;

public class InvoiceResponse {
    
    private String invoiceId;
    private String patientName;
    private int patientAge;
    private String patientIdCard;
    private String doctorName;
    private String invoiceDate;
    
    // Información del seguro médico
    private String insuranceCompany;
    private String policyNumber;
    private int policyRemainingDays;
    private String policyEndDate;
    
    // Líneas de la factura
    private List<InvoiceLineInfo> lines;
    
    // Totales y pagos
    private long subtotal;
    private long copay;
    private long insuranceCoverage;
    private long totalPatientPayment;
    
    // Estado de la factura
    private String status;
    private boolean paid;
    private String paymentDate;
    private String paymentMethod;
    
    // Default constructor
    public InvoiceResponse() {}
    
    // Constructor básico
    public InvoiceResponse(String invoiceId, String patientName, int patientAge, String patientIdCard,
                          String doctorName, String invoiceDate, long subtotal, long copay) {
        this.invoiceId = invoiceId;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientIdCard = patientIdCard;
        this.doctorName = doctorName;
        this.invoiceDate = invoiceDate;
        this.subtotal = subtotal;
        this.copay = copay;
        this.insuranceCoverage = subtotal - copay;
        this.totalPatientPayment = copay;
    }
    
    // Constructor completo
    public InvoiceResponse(String invoiceId, String patientName, int patientAge, String patientIdCard,
                          String doctorName, String invoiceDate, String insuranceCompany, String policyNumber,
                          int policyRemainingDays, String policyEndDate, List<InvoiceLineInfo> lines,
                          long subtotal, long copay, long insuranceCoverage, long totalPatientPayment,
                          String status, boolean paid, String paymentDate, String paymentMethod) {
        this.invoiceId = invoiceId;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientIdCard = patientIdCard;
        this.doctorName = doctorName;
        this.invoiceDate = invoiceDate;
        this.insuranceCompany = insuranceCompany;
        this.policyNumber = policyNumber;
        this.policyRemainingDays = policyRemainingDays;
        this.policyEndDate = policyEndDate;
        this.lines = lines;
        this.subtotal = subtotal;
        this.copay = copay;
        this.insuranceCoverage = insuranceCoverage;
        this.totalPatientPayment = totalPatientPayment;
        this.status = status;
        this.paid = paid;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }
    
    // Getters
    public String getInvoiceId() { return invoiceId; }
    public String getPatientName() { return patientName; }
    public int getPatientAge() { return patientAge; }
    public String getPatientIdCard() { return patientIdCard; }
    public String getDoctorName() { return doctorName; }
    public String getInvoiceDate() { return invoiceDate; }
    public String getInsuranceCompany() { return insuranceCompany; }
    public String getPolicyNumber() { return policyNumber; }
    public int getPolicyRemainingDays() { return policyRemainingDays; }
    public String getPolicyEndDate() { return policyEndDate; }
    public List<InvoiceLineInfo> getLines() { return lines; }
    public long getSubtotal() { return subtotal; }
    public long getCopay() { return copay; }
    public long getInsuranceCoverage() { return insuranceCoverage; }
    public long getTotalPatientPayment() { return totalPatientPayment; }
    public String getStatus() { return status; }
    public boolean isPaid() { return paid; }
    public String getPaymentDate() { return paymentDate; }
    public String getPaymentMethod() { return paymentMethod; }
    
    // Setters
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setPatientAge(int patientAge) { this.patientAge = patientAge; }
    public void setPatientIdCard(String patientIdCard) { this.patientIdCard = patientIdCard; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setInvoiceDate(String invoiceDate) { this.invoiceDate = invoiceDate; }
    public void setInsuranceCompany(String insuranceCompany) { this.insuranceCompany = insuranceCompany; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    public void setPolicyRemainingDays(int policyRemainingDays) { this.policyRemainingDays = policyRemainingDays; }
    public void setPolicyEndDate(String policyEndDate) { this.policyEndDate = policyEndDate; }
    public void setLines(List<InvoiceLineInfo> lines) { this.lines = lines; }
    public void setSubtotal(long subtotal) { this.subtotal = subtotal; }
    public void setCopay(long copay) { this.copay = copay; }
    public void setInsuranceCoverage(long insuranceCoverage) { this.insuranceCoverage = insuranceCoverage; }
    public void setTotalPatientPayment(long totalPatientPayment) { this.totalPatientPayment = totalPatientPayment; }
    public void setStatus(String status) { this.status = status; }
    public void setPaid(boolean paid) { this.paid = paid; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    // Utility methods
    public boolean hasInsurance() {
        return insuranceCompany != null && !insuranceCompany.isBlank();
    }
    
    public boolean hasActiveInsurance() {
        return hasInsurance() && policyRemainingDays > 0;
    }
    
    public boolean isInsuranceExpired() {
        return hasInsurance() && policyRemainingDays <= 0;
    }
    
    public boolean isInsuranceExpiringSoon() {
        return hasInsurance() && policyRemainingDays > 0 && policyRemainingDays <= 30;
    }
    
    public boolean hasLines() {
        return lines != null && !lines.isEmpty();
    }
    
    public int getLineCount() {
        return lines != null ? lines.size() : 0;
    }
    
    public boolean isPending() {
        return "PENDING".equals(status);
    }
    
    public boolean isProcessed() {
        return "PROCESSED".equals(status);
    }
    
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
    
    public boolean hasCopay() {
        return copay > 0;
    }
    
    public boolean hasInsuranceCoverage() {
        return insuranceCoverage > 0;
    }
    
    public double getCopayPercentage() {
        return subtotal > 0 ? (double) copay / subtotal * 100 : 0;
    }
    
    public double getInsuranceCoveragePercentage() {
        return subtotal > 0 ? (double) insuranceCoverage / subtotal * 100 : 0;
    }
    
    // Formateo de monedas
    public String getFormattedSubtotal() {
        return String.format("$%,d", subtotal);
    }
    
    public String getFormattedCopay() {
        return String.format("$%,d", copay);
    }
    
    public String getFormattedInsuranceCoverage() {
        return String.format("$%,d", insuranceCoverage);
    }
    
    public String getFormattedTotalPatientPayment() {
        return String.format("$%,d", totalPatientPayment);
    }
    
    // Clase interna para líneas de factura
    public static class InvoiceLineInfo {
        private String description;
        private long amount;
        private String itemType;
        private String itemName;
        private String details;
        
        public InvoiceLineInfo() {}
        
        public InvoiceLineInfo(String description, long amount) {
            this.description = description;
            this.amount = amount;
        }
        
        public InvoiceLineInfo(String description, long amount, String itemType, String itemName, String details) {
            this.description = description;
            this.amount = amount;
            this.itemType = itemType;
            this.itemName = itemName;
            this.details = details;
        }
        
        // Getters and Setters
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public long getAmount() { return amount; }
        public void setAmount(long amount) { this.amount = amount; }
        public String getItemType() { return itemType; }
        public void setItemType(String itemType) { this.itemType = itemType; }
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public String getDetails() { return details; }
        public void setDetails(String details) { this.details = details; }
        
        public boolean isMedication() {
            return "MEDICATION".equals(itemType);
        }
        
        public boolean isProcedure() {
            return "PROCEDURE".equals(itemType);
        }
        
        public boolean isDiagnostic() {
            return "DIAGNOSTIC".equals(itemType);
        }
        
        public String getFormattedAmount() {
            return String.format("$%,d", amount);
        }
        
        @Override
        public String toString() {
            return "InvoiceLineInfo{" +
                    "description='" + description + '\'' +
                    ", amount=" + getFormattedAmount() +
                    ", itemType='" + itemType + '\'' +
                    '}';
        }
    }
    
    @Override
    public String toString() {
        return "InvoiceResponse{" +
                "invoiceId='" + invoiceId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", patientAge=" + patientAge +
                ", doctorName='" + doctorName + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", hasInsurance=" + hasInsurance() +
                ", subtotal=" + getFormattedSubtotal() +
                ", copay=" + getFormattedCopay() +
                ", insuranceCoverage=" + getFormattedInsuranceCoverage() +
                ", totalPatientPayment=" + getFormattedTotalPatientPayment() +
                ", status='" + status + '\'' +
                ", paid=" + paid +
                '}';
    }
}