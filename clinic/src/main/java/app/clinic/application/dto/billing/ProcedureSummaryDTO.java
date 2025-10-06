package app.clinic.application.dto.billing;

/**
 * Data Transfer Object for procedure summary information in billing.
 * Used for API responses containing procedure summary data.
 */
public class ProcedureSummaryDTO {
    private String procedureName;
    private String cost;

    // Default constructor
    public ProcedureSummaryDTO() {}

    // Constructor with parameters
    public ProcedureSummaryDTO(String procedureName, String cost) {
        this.procedureName = procedureName;
        this.cost = cost;
    }

    // Getters and Setters
    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return String.format("ProcedureSummaryDTO{procedureName='%s', cost='%s'}",
                           procedureName, cost);
    }
}