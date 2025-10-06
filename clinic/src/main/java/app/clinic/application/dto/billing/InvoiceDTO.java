package app.clinic.application.dto.billing;

import java.util.List;

/**
 * Data Transfer Object for invoice information.
 * Used for API responses containing invoice data.
 */
public class InvoiceDTO {
    private String invoiceNumber;
    private String patientCedula;
    private String patientName;
    private BillingDTO billingDetails;
    private String totalAmount;
    private String copaymentAmount;
    private String insuranceCoverage;
    private String patientResponsibility;
    private String billingDate;
    private String dueDate;
    private String status;
    private String notes;
    private Integer year;
    private List<String> orderSummaries;

    // Default constructor
    public InvoiceDTO() {}

    // Constructor with parameters
    public InvoiceDTO(String invoiceNumber, String patientCedula, String patientName,
                     BillingDTO billingDetails, String totalAmount, String copaymentAmount,
                     String insuranceCoverage, String patientResponsibility,
                     String billingDate, String dueDate, String status, String notes,
                     Integer year, List<String> orderSummaries) {
        this.invoiceNumber = invoiceNumber;
        this.patientCedula = patientCedula;
        this.patientName = patientName;
        this.billingDetails = billingDetails;
        this.totalAmount = totalAmount;
        this.copaymentAmount = copaymentAmount;
        this.insuranceCoverage = insuranceCoverage;
        this.patientResponsibility = patientResponsibility;
        this.billingDate = billingDate;
        this.dueDate = dueDate;
        this.status = status;
        this.notes = notes;
        this.year = year;
        this.orderSummaries = orderSummaries;
    }

    // Getters and Setters
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BillingDTO getBillingDetails() {
        return billingDetails;
    }

    public void setBillingDetails(BillingDTO billingDetails) {
        this.billingDetails = billingDetails;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

    public String getPatientCedula() {
        return patientCedula;
    }

    public void setPatientCedula(String patientCedula) {
        this.patientCedula = patientCedula;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getCopaymentAmount() {
        return copaymentAmount;
    }

    public void setCopaymentAmount(String copaymentAmount) {
        this.copaymentAmount = copaymentAmount;
    }

    public String getInsuranceCoverage() {
        return insuranceCoverage;
    }

    public void setInsuranceCoverage(String insuranceCoverage) {
        this.insuranceCoverage = insuranceCoverage;
    }

    public String getPatientResponsibility() {
        return patientResponsibility;
    }

    public void setPatientResponsibility(String patientResponsibility) {
        this.patientResponsibility = patientResponsibility;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<String> getOrderSummaries() {
        return orderSummaries;
    }

    public void setOrderSummaries(List<String> orderSummaries) {
        this.orderSummaries = orderSummaries;
    }

    @Override
    public String toString() {
        return String.format("InvoiceDTO{invoiceNumber='%s', patientCedula='%s', patientName='%s', totalAmount='%s', status='%s'}",
                           invoiceNumber, patientCedula, patientName, totalAmount, status);
    }
}