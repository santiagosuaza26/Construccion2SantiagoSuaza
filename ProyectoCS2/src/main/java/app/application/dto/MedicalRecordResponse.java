package app.application.dto;

public class MedicalRecordResponse {
    private String id;
    private String patientId;
    private String details;

    public MedicalRecordResponse(String id, String patientId, String details) {
        this.id = id;
        this.patientId = patientId;
        this.details = details;
    }

    // Getters
    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDetails() { return details; }
}
