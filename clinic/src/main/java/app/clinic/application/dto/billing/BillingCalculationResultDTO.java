package app.clinic.application.dto.billing;

/**
 * Data Transfer Object for billing calculation results.
 * Used for API responses containing billing calculation data.
 */
public class BillingCalculationResultDTO {
    private String totalCost;
    private String copaymentAmount;
    private String insuranceCoverageAmount;
    private boolean requiresCopayment;
    private boolean copaymentLimitExceeded;
    private String copaymentLimitMessage;

    // Default constructor
    public BillingCalculationResultDTO() {}

    // Constructor with parameters
    public BillingCalculationResultDTO(String totalCost, String copaymentAmount,
                                     String insuranceCoverageAmount, boolean requiresCopayment,
                                     boolean copaymentLimitExceeded, String copaymentLimitMessage) {
        this.totalCost = totalCost;
        this.copaymentAmount = copaymentAmount;
        this.insuranceCoverageAmount = insuranceCoverageAmount;
        this.requiresCopayment = requiresCopayment;
        this.copaymentLimitExceeded = copaymentLimitExceeded;
        this.copaymentLimitMessage = copaymentLimitMessage;
    }

    // Getters and Setters
    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
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

    public boolean isRequiresCopayment() {
        return requiresCopayment;
    }

    public void setRequiresCopayment(boolean requiresCopayment) {
        this.requiresCopayment = requiresCopayment;
    }

    public boolean isCopaymentLimitExceeded() {
        return copaymentLimitExceeded;
    }

    public void setCopaymentLimitExceeded(boolean copaymentLimitExceeded) {
        this.copaymentLimitExceeded = copaymentLimitExceeded;
    }

    public String getCopaymentLimitMessage() {
        return copaymentLimitMessage;
    }

    public void setCopaymentLimitMessage(String copaymentLimitMessage) {
        this.copaymentLimitMessage = copaymentLimitMessage;
    }

    @Override
    public String toString() {
        return String.format("BillingCalculationResultDTO{totalCost='%s', copaymentAmount='%s', requiresCopayment=%s}",
                           totalCost, copaymentAmount, requiresCopayment);
    }
}