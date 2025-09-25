package app.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class GenerateInvoiceRequest {
    
    @NotBlank(message = "Order number is required")
    @Pattern(regexp = "\\d{1,6}", message = "Order number must be numeric with maximum 6 digits")
    private String orderNumber;
    
    @NotBlank(message = "Patient ID card is required")
    @Pattern(regexp = "\\d+", message = "Patient ID card must contain only numbers")
    private String patientIdCard;
    
    @NotBlank(message = "Doctor name is required")
    private String doctorName;
    
    @NotBlank(message = "Invoice date is required")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Invoice date must be in DD/MM/YYYY format")
    private String invoiceDate;
    
    // Información adicional para la factura
    private String notes;
    
    private Boolean printInvoice = false;
    
    private Boolean sendByEmail = false;
    
    // Default constructor
    public GenerateInvoiceRequest() {}
    
    // Constructor with required fields
    public GenerateInvoiceRequest(String orderNumber, String patientIdCard, String doctorName, String invoiceDate) {
        this.orderNumber = orderNumber;
        this.patientIdCard = patientIdCard;
        this.doctorName = doctorName;
        this.invoiceDate = invoiceDate;
    }
    
    // Constructor with all fields
    public GenerateInvoiceRequest(String orderNumber, String patientIdCard, String doctorName, 
                                String invoiceDate, String notes, Boolean printInvoice, Boolean sendByEmail) {
        this.orderNumber = orderNumber;
        this.patientIdCard = patientIdCard;
        this.doctorName = doctorName;
        this.invoiceDate = invoiceDate;
        this.notes = notes;
        this.printInvoice = printInvoice;
        this.sendByEmail = sendByEmail;
    }
    
    // Getters
    public String getOrderNumber() { return orderNumber; }
    public String getPatientIdCard() { return patientIdCard; }
    public String getDoctorName() { return doctorName; }
    public String getInvoiceDate() { return invoiceDate; }
    public String getNotes() { return notes; }
    public Boolean getPrintInvoice() { return printInvoice; }
    public Boolean getSendByEmail() { return sendByEmail; }
    
    // Setters
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public void setPatientIdCard(String patientIdCard) { this.patientIdCard = patientIdCard; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setInvoiceDate(String invoiceDate) { this.invoiceDate = invoiceDate; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setPrintInvoice(Boolean printInvoice) { this.printInvoice = printInvoice; }
    public void setSendByEmail(Boolean sendByEmail) { this.sendByEmail = sendByEmail; }
    
    // Utility methods
    public boolean hasNotes() {
        return notes != null && !notes.isBlank();
    }
    
    public boolean shouldPrint() {
        return printInvoice != null && printInvoice;
    }
    
    public boolean shouldSendByEmail() {
        return sendByEmail != null && sendByEmail;
    }
    
    @Override
    public String toString() {
        return "GenerateInvoiceRequest{" +
                "orderNumber='" + orderNumber + '\'' +
                ", patientIdCard='" + patientIdCard + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", hasNotes=" + hasNotes() +
                ", shouldPrint=" + shouldPrint() +
                ", shouldSendByEmail=" + shouldSendByEmail() +
                '}';
    }
}