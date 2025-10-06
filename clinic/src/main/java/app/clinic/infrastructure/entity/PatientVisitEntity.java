package app.clinic.infrastructure.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * JPA entity representing patient visits table in the database.
 * Maps domain PatientVisit objects to database records.
 */
@Entity
@Table(name = "patient_visits")
public class PatientVisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_cedula", nullable = false, length = 20)
    private String patientCedula;

    @Column(name = "visit_date_time", nullable = false)
    private LocalDateTime visitDateTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vital_signs_id")
    private VitalSignsEntity vitalSigns;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nurse_record_id")
    private NurseRecordEntity nurseRecord;

    @Column(name = "completed", nullable = false)
    private boolean completed = false;

    // Default constructor
    public PatientVisitEntity() {}

    // Constructor with parameters
    public PatientVisitEntity(String patientCedula, LocalDateTime visitDateTime,
                             VitalSignsEntity vitalSigns, NurseRecordEntity nurseRecord, boolean completed) {
        this.patientCedula = patientCedula;
        this.visitDateTime = visitDateTime;
        this.vitalSigns = vitalSigns;
        this.nurseRecord = nurseRecord;
        this.completed = completed;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientCedula() {
        return patientCedula;
    }

    public void setPatientCedula(String patientCedula) {
        this.patientCedula = patientCedula;
    }

    public LocalDateTime getVisitDateTime() {
        return visitDateTime;
    }

    public void setVisitDateTime(LocalDateTime visitDateTime) {
        this.visitDateTime = visitDateTime;
    }

    public VitalSignsEntity getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(VitalSignsEntity vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public NurseRecordEntity getNurseRecord() {
        return nurseRecord;
    }

    public void setNurseRecord(NurseRecordEntity nurseRecord) {
        this.nurseRecord = nurseRecord;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}