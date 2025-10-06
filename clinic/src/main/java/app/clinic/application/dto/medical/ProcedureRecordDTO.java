package app.clinic.application.dto.medical;

/**
 * Data Transfer Object for procedure information in medical records.
 * Contains procedure details that can be associated with a medical record entry.
 */
public class ProcedureRecordDTO {
    private String procedureName;
    private Integer numberOfTimes;
    private String frequency;
    private String instructions;
    private Boolean requiresSpecialistAssistance;

    // Default constructor
    public ProcedureRecordDTO() {}

    // Constructor with parameters
    public ProcedureRecordDTO(String procedureName, Integer numberOfTimes, String frequency,
                             String instructions, Boolean requiresSpecialistAssistance) {
        this.procedureName = procedureName;
        this.numberOfTimes = numberOfTimes;
        this.frequency = frequency;
        this.instructions = instructions;
        this.requiresSpecialistAssistance = requiresSpecialistAssistance;
    }

    // Getters and Setters
    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public Integer getNumberOfTimes() {
        return numberOfTimes;
    }

    public void setNumberOfTimes(Integer numberOfTimes) {
        this.numberOfTimes = numberOfTimes;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
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
        return String.format("ProcedureRecordDTO{name='%s', times=%d, frequency='%s'}",
                           procedureName, numberOfTimes, frequency);
    }
}