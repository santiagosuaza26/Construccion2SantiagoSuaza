package app.clinic.application.dto.medical;

/**
 * Data Transfer Object for diagnostic aid information in medical records.
 * Contains diagnostic aid details that can be associated with a medical record entry.
 */
public class DiagnosticAidRecordDTO {
    private String diagnosticAidName;
    private Integer quantity;
    private String purpose;
    private String instructions;
    private Boolean requiresSpecialistAssistance;

    // Default constructor
    public DiagnosticAidRecordDTO() {}

    // Constructor with parameters
    public DiagnosticAidRecordDTO(String diagnosticAidName, Integer quantity, String purpose,
                                 String instructions, Boolean requiresSpecialistAssistance) {
        this.diagnosticAidName = diagnosticAidName;
        this.quantity = quantity;
        this.purpose = purpose;
        this.instructions = instructions;
        this.requiresSpecialistAssistance = requiresSpecialistAssistance;
    }

    // Getters and Setters
    public String getDiagnosticAidName() {
        return diagnosticAidName;
    }

    public void setDiagnosticAidName(String diagnosticAidName) {
        this.diagnosticAidName = diagnosticAidName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Boolean getRequiresSpecialistAssistance() {
        return requiresSpecialistAssistance;
    }

    public void setRequiresSpecialistAssistance(Boolean requiresSpecialistAssistance) {
        this.requiresSpecialistAssistance = requiresSpecialistAssistance;
    }

    @Override
    public String toString() {
        return String.format("DiagnosticAidRecordDTO{name='%s', quantity=%d, purpose='%s'}",
                           diagnosticAidName, quantity, purpose);
    }
}