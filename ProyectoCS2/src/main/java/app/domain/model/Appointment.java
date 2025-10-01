package app.domain.model;

import java.time.LocalDateTime;

/**
 * Modelo de dominio para representar una cita médica
 * Sigue principios SOLID:
 * - SRP: Representa una cita médica con toda su información
 * - OCP: Puede extenderse para diferentes tipos de citas
 */
public class Appointment {
    private final String id;
    private final String patientIdCard;
    private final String doctorIdCard;
    private final LocalDateTime appointmentDateTime;
    private final String appointmentType;
    private final String status;
    private final String notes;
    private final LocalDateTime createdAt;
    private final String createdBy;

    public Appointment(String id, String patientIdCard, String doctorIdCard,
                        LocalDateTime appointmentDateTime, String appointmentType,
                        String status, String notes, String createdBy) {
        this.id = id;
        this.patientIdCard = patientIdCard;
        this.doctorIdCard = doctorIdCard;
        this.appointmentDateTime = appointmentDateTime;
        this.appointmentType = appointmentType;
        this.status = status;
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
        this.createdBy = createdBy;
    }

    // Getters
    public String getId() { return id; }
    public String getPatientIdCard() { return patientIdCard; }
    public String getDoctorIdCard() { return doctorIdCard; }
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public String getAppointmentType() { return appointmentType; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCreatedBy() { return createdBy; }

    /**
     * Verifica si la cita está programada para el futuro
     */
    public boolean isFutureAppointment() {
        return appointmentDateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Verifica si la cita está activa (confirmada o pendiente)
     */
    public boolean isActive() {
        return "CONFIRMED".equals(status) || "PENDING".equals(status);
    }

    /**
     * Verifica si la cita puede ser cancelada
     */
    public boolean canBeCancelled() {
        return isFutureAppointment() && isActive();
    }
}