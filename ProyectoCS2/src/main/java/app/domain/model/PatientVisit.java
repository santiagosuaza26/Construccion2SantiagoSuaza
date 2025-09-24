package app.domain.model;

import java.time.LocalDateTime;

public class PatientVisit {
    private final String visitId;
    private final String patientIdCard;
    private final String attendingStaffIdCard;
    private final Role attendingStaffRole;
    private final LocalDateTime visitDateTime;
    private final String notes;
    private final VitalSigns vitalSigns;
    private final String relatedOrderNumber;

    public PatientVisit(String visitId, String patientIdCard, String attendingStaffIdCard,
                        Role attendingStaffRole, LocalDateTime visitDateTime, String notes,
                        VitalSigns vitalSigns, String relatedOrderNumber) {
        this.visitId = visitId;
        this.patientIdCard = patientIdCard;
        this.attendingStaffIdCard = attendingStaffIdCard;
        this.attendingStaffRole = attendingStaffRole;
        this.visitDateTime = visitDateTime;
        this.notes = notes;
        this.vitalSigns = vitalSigns;
        this.relatedOrderNumber = relatedOrderNumber;
        validate();
    }

    // Getters
    public String getVisitId() { return visitId; }
    public String getPatientIdCard() { return patientIdCard; }
    public String getAttendingStaffIdCard() { return attendingStaffIdCard; }
    public Role getAttendingStaffRole() { return attendingStaffRole; }
    public LocalDateTime getVisitDateTime() { return visitDateTime; }
    public String getNotes() { return notes; }
    public VitalSigns getVitalSigns() { return vitalSigns; }
    public String getRelatedOrderNumber() { return relatedOrderNumber; }

    private void validate() {
        if (visitId == null || visitId.isBlank()) {
            throw new IllegalArgumentException("Visit id required");
        }
        if (patientIdCard == null || !patientIdCard.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid patient id");
        }
        if (attendingStaffIdCard == null || !attendingStaffIdCard.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid staff id");
        }
        if (visitDateTime == null) {
            throw new IllegalArgumentException("Visit date time required");
        }
        if (attendingStaffRole == null) {
            throw new IllegalArgumentException("Attending staff role required");
        }
    }
}