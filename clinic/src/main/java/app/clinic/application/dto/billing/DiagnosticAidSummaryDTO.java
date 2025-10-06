package app.clinic.application.dto.billing;

/**
 * Data Transfer Object for diagnostic aid summary information in billing.
 * Used for API responses containing diagnostic aid summary data.
 */
public class DiagnosticAidSummaryDTO {
    private String diagnosticAidName;
    private String cost;

    // Default constructor
    public DiagnosticAidSummaryDTO() {}

    // Constructor with parameters
    public DiagnosticAidSummaryDTO(String diagnosticAidName, String cost) {
        this.diagnosticAidName = diagnosticAidName;
        this.cost = cost;
    }

    // Getters and Setters
    public String getDiagnosticAidName() {
        return diagnosticAidName;
    }

    public void setDiagnosticAidName(String diagnosticAidName) {
        this.diagnosticAidName = diagnosticAidName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return String.format("DiagnosticAidSummaryDTO{diagnosticAidName='%s', cost='%s'}",
                           diagnosticAidName, cost);
    }
}