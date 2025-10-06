package app.clinic.application.dto.billing;

import java.util.List;

/**
 * Data Transfer Object for order summary information in billing.
 * Used for API responses containing order summary data.
 */
public class OrderSummaryDTO {
    private String orderNumber;
    private List<MedicationSummaryDTO> medications;
    private List<ProcedureSummaryDTO> procedures;
    private List<DiagnosticAidSummaryDTO> diagnosticAids;

    // Default constructor
    public OrderSummaryDTO() {}

    // Constructor with parameters
    public OrderSummaryDTO(String orderNumber, List<MedicationSummaryDTO> medications,
                          List<ProcedureSummaryDTO> procedures, List<DiagnosticAidSummaryDTO> diagnosticAids) {
        this.orderNumber = orderNumber;
        this.medications = medications;
        this.procedures = procedures;
        this.diagnosticAids = diagnosticAids;
    }

    // Getters and Setters
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<MedicationSummaryDTO> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationSummaryDTO> medications) {
        this.medications = medications;
    }

    public List<ProcedureSummaryDTO> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<ProcedureSummaryDTO> procedures) {
        this.procedures = procedures;
    }

    public List<DiagnosticAidSummaryDTO> getDiagnosticAids() {
        return diagnosticAids;
    }

    public void setDiagnosticAids(List<DiagnosticAidSummaryDTO> diagnosticAids) {
        this.diagnosticAids = diagnosticAids;
    }

    @Override
    public String toString() {
        return String.format("OrderSummaryDTO{orderNumber='%s', medications=%d, procedures=%d, diagnosticAids=%d}",
                           orderNumber,
                           medications != null ? medications.size() : 0,
                           procedures != null ? procedures.size() : 0,
                           diagnosticAids != null ? diagnosticAids.size() : 0);
    }
}