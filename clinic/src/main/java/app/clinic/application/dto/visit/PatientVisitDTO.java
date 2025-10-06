package app.clinic.application.dto.visit;

/**
 * Data Transfer Object for patient visit information.
 * Used for API responses containing patient visit data.
 */
public class PatientVisitDTO {
    private String id;
    private String patientCedula;
    private String visitDateTime;
    private VitalSignsDTO vitalSigns;
    private NurseRecordDTO nurseRecord;
    private boolean completed;

    // Default constructor
    public PatientVisitDTO() {}

    // Constructor with parameters
    public PatientVisitDTO(String id, String patientCedula, String visitDateTime,
                          VitalSignsDTO vitalSigns, NurseRecordDTO nurseRecord, boolean completed) {
        this.id = id;
        this.patientCedula = patientCedula;
        this.visitDateTime = visitDateTime;
        this.vitalSigns = vitalSigns;
        this.nurseRecord = nurseRecord;
        this.completed = completed;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientCedula() {
        return patientCedula;
    }

    public void setPatientCedula(String patientCedula) {
        this.patientCedula = patientCedula;
    }

    public String getVisitDateTime() {
        return visitDateTime;
    }

    public void setVisitDateTime(String visitDateTime) {
        this.visitDateTime = visitDateTime;
    }

    public VitalSignsDTO getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(VitalSignsDTO vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public NurseRecordDTO getNurseRecord() {
        return nurseRecord;
    }

    public void setNurseRecord(NurseRecordDTO nurseRecord) {
        this.nurseRecord = nurseRecord;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return String.format("PatientVisitDTO{id='%s', patientCedula='%s', visitDateTime='%s', completed=%s}",
                           id, patientCedula, visitDateTime, completed);
    }
}