package app.clinic.infrastructure.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity representing appointments table in the database.
 * Maps domain Appointment objects to database records.
 */
@Entity
@Table(name = "appointments")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id", nullable = false, unique = true, length = 20)
    private String appointmentId;

    @Column(name = "patient_cedula", nullable = false, length = 20)
    private String patientCedula;

    @Column(name = "doctor_cedula", nullable = false, length = 10)
    private String doctorCedula;

    @Column(name = "appointment_datetime", nullable = false)
    private LocalDateTime appointmentDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "notes", length = 500)
    private String notes;

    // Default constructor
    public AppointmentEntity() {}

    // Constructor with parameters
    public AppointmentEntity(String appointmentId, String patientCedula, String doctorCedula,
                           LocalDateTime appointmentDateTime, AppointmentStatus status, String reason, String notes) {
        this.appointmentId = appointmentId;
        this.patientCedula = patientCedula;
        this.doctorCedula = doctorCedula;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status != null ? status : AppointmentStatus.PROGRAMADA;
        this.reason = reason;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientCedula() {
        return patientCedula;
    }

    public void setPatientCedula(String patientCedula) {
        this.patientCedula = patientCedula;
    }

    public String getDoctorCedula() {
        return doctorCedula;
    }

    public void setDoctorCedula(String doctorCedula) {
        this.doctorCedula = doctorCedula;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Enumeration for appointment status in the database.
     */
    public enum AppointmentStatus {
        PROGRAMADA,
        CONFIRMADA,
        EN_CURSO,
        COMPLETADA,
        CANCELADA,
        NO_ASISTIO
    }
}