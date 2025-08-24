package app.application.dto;

public class MedicalRecordRequest {
    private String id;
    private String patientId;
    private String details;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
