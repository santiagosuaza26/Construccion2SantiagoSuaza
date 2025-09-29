package app.infrastructure.adapter.jpa.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_visits")
public class PatientVisitEntity {

    @Id
    private String visitId;

    @Column(nullable = false)
    private String patientIdCard;

    @Column(nullable = false)
    private String attendingStaffIdCard;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private app.domain.model.Role attendingStaffRole;

    @Column(nullable = false)
    private LocalDateTime visitDateTime;

    @Column(length = 1000)
    private String notes;

    @Embedded
    private VitalSignsEmbedded vitalSigns;

    private String relatedOrderNumber;

    // Constructor vacío para JPA
    public PatientVisitEntity() {}

    // Constructor con parámetros
    public PatientVisitEntity(String visitId, String patientIdCard, String attendingStaffIdCard,
                             app.domain.model.Role attendingStaffRole, LocalDateTime visitDateTime,
                             String notes, VitalSignsEmbedded vitalSigns, String relatedOrderNumber) {
        this.visitId = visitId;
        this.patientIdCard = patientIdCard;
        this.attendingStaffIdCard = attendingStaffIdCard;
        this.attendingStaffRole = attendingStaffRole;
        this.visitDateTime = visitDateTime;
        this.notes = notes;
        this.vitalSigns = vitalSigns;
        this.relatedOrderNumber = relatedOrderNumber;
    }

    // Getters y Setters
    public String getVisitId() { return visitId; }
    public void setVisitId(String visitId) { this.visitId = visitId; }

    public String getPatientIdCard() { return patientIdCard; }
    public void setPatientIdCard(String patientIdCard) { this.patientIdCard = patientIdCard; }

    public String getAttendingStaffIdCard() { return attendingStaffIdCard; }
    public void setAttendingStaffIdCard(String attendingStaffIdCard) { this.attendingStaffIdCard = attendingStaffIdCard; }

    public app.domain.model.Role getAttendingStaffRole() { return attendingStaffRole; }
    public void setAttendingStaffRole(app.domain.model.Role attendingStaffRole) { this.attendingStaffRole = attendingStaffRole; }

    public LocalDateTime getVisitDateTime() { return visitDateTime; }
    public void setVisitDateTime(LocalDateTime visitDateTime) { this.visitDateTime = visitDateTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public VitalSignsEmbedded getVitalSigns() { return vitalSigns; }
    public void setVitalSigns(VitalSignsEmbedded vitalSigns) { this.vitalSigns = vitalSigns; }

    public String getRelatedOrderNumber() { return relatedOrderNumber; }
    public void setRelatedOrderNumber(String relatedOrderNumber) { this.relatedOrderNumber = relatedOrderNumber; }
}