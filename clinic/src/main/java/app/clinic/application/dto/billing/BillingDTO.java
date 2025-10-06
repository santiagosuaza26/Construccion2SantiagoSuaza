package app.clinic.application.dto.billing;

import java.util.List;

/**
 * Data Transfer Object for billing information.
 * Used for API responses containing billing data.
 */
public class BillingDTO {
    private String patientName;
    private Integer patientAge;
    private String patientCedula;
    private String insuranceCompany;
    private String policyNumber;
    private Integer validityDays;
    private String expirationDate;
    private List<OrderSummaryDTO> orderSummaries;
    private String totalAmount;
    private String copaymentAmount;
    private String insuranceCoverageAmount;

    // Default constructor
    public BillingDTO() {}

    // Constructor with parameters
    public BillingDTO(String patientName, Integer patientAge, String patientCedula,
                     String insuranceCompany, String policyNumber, Integer validityDays,
                     String expirationDate, List<OrderSummaryDTO> orderSummaries,
                     String totalAmount, String copaymentAmount, String insuranceCoverageAmount) {
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientCedula = patientCedula;
        this.insuranceCompany = insuranceCompany;
        this.policyNumber = policyNumber;
        this.validityDays = validityDays;
        this.expirationDate = expirationDate;
        this.orderSummaries = orderSummaries;
        this.totalAmount = totalAmount;
        this.copaymentAmount = copaymentAmount;
        this.insuranceCoverageAmount = insuranceCoverageAmount;
    }

    // Getters and Setters
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientCedula() {
        return patientCedula;
    }

    public void setPatientCedula(String patientCedula) {
        this.patientCedula = patientCedula;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Integer getValidityDays() {
        return validityDays;
    }

    public void setValidityDays(Integer validityDays) {
        this.validityDays = validityDays;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<OrderSummaryDTO> getOrderSummaries() {
        return orderSummaries;
    }

    public void setOrderSummaries(List<OrderSummaryDTO> orderSummaries) {
        this.orderSummaries = orderSummaries;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCopaymentAmount() {
        return copaymentAmount;
    }

    public void setCopaymentAmount(String copaymentAmount) {
        this.copaymentAmount = copaymentAmount;
    }

    public String getInsuranceCoverageAmount() {
        return insuranceCoverageAmount;
    }

    public void setInsuranceCoverageAmount(String insuranceCoverageAmount) {
        this.insuranceCoverageAmount = insuranceCoverageAmount;
    }

    @Override
    public String toString() {
        return String.format("BillingDTO{patientName='%s', patientCedula='%s', totalAmount='%s'}",
                           patientName, patientCedula, totalAmount);
    }
}