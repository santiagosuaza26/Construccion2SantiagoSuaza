package app.clinic.application.dto.visit;

/**
 * Data Transfer Object for nurse record information.
 * Used for API responses containing nurse record data.
 */
public class NurseRecordDTO {
    private String administeredMedications;
    private String performedProcedures;
    private String observations;

    // Default constructor
    public NurseRecordDTO() {}

    // Constructor with parameters
    public NurseRecordDTO(String administeredMedications, String performedProcedures, String observations) {
        this.administeredMedications = administeredMedications;
        this.performedProcedures = performedProcedures;
        this.observations = observations;
    }

    // Getters and Setters
    public String getAdministeredMedications() {
        return administeredMedications;
    }

    public void setAdministeredMedications(String administeredMedications) {
        this.administeredMedications = administeredMedications;
    }

    public String getPerformedProcedures() {
        return performedProcedures;
    }

    public void setPerformedProcedures(String performedProcedures) {
        this.performedProcedures = performedProcedures;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @Override
    public String toString() {
        return String.format("NurseRecordDTO{administeredMedications='%s', performedProcedures='%s', observations='%s'}",
                           administeredMedications, performedProcedures, observations);
    }
}