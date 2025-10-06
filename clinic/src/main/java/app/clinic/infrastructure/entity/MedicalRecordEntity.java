package app.clinic.infrastructure.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

/**
 * JPA entity representing medical records table in the database.
 * Maps domain PatientRecord objects to database records.
 */
@Entity
@Table(name = "medical_records")
public class MedicalRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_cedula", nullable = false, length = 20)
    private String patientCedula;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "doctor_cedula", nullable = false, length = 10)
    private String doctorCedula;

    @Column(name = "consultation_reason", nullable = false, length = 200)
    private String consultationReason;

    @Column(name = "symptoms", nullable = false, columnDefinition = "TEXT")
    private String symptoms;

    @Column(name = "diagnosis", nullable = false, columnDefinition = "TEXT")
    private String diagnosis;

    @Lob
    @Column(name = "additional_data", columnDefinition = "TEXT")
    private String additionalData;

    // Default constructor
    public MedicalRecordEntity() {}

    // Constructor with parameters
    public MedicalRecordEntity(String patientCedula, LocalDate recordDate, String doctorCedula,
                              String consultationReason, String symptoms, String diagnosis, String additionalData) {
        this.patientCedula = patientCedula;
        this.recordDate = recordDate;
        this.doctorCedula = doctorCedula;
        this.consultationReason = consultationReason;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.additionalData = additionalData;
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

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public String getDoctorCedula() {
        return doctorCedula;
    }

    public void setDoctorCedula(String doctorCedula) {
        this.doctorCedula = doctorCedula;
    }

    public String getConsultationReason() {
        return consultationReason;
    }

    public void setConsultationReason(String consultationReason) {
        this.consultationReason = consultationReason;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }
}