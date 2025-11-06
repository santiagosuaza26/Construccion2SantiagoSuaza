package app.clinic.domain.model.entities;

import java.time.LocalDateTime;

import app.clinic.domain.model.valueobject.AppointmentStatus;
import app.clinic.domain.model.valueobject.Id;

public class Appointment {
    private final Id id;
    private final Id patientId;
    private final Id doctorId;
    private final LocalDateTime dateTime;
    private final String reason;
    private AppointmentStatus status;

    public Appointment(Id id, Id patientId, Id doctorId, LocalDateTime dateTime, String reason) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.reason = reason;
        this.status = AppointmentStatus.SCHEDULED;
    }

    public Appointment(Id patientId, Id doctorId, LocalDateTime dateTime, String reason) {
        this(null, patientId, doctorId, dateTime, reason);
    }

    public Id getId() {
        return id;
    }

    public Id getPatientId() {
        return patientId;
    }

    public Id getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getReason() {
        return reason;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void confirm() {
        if (status == AppointmentStatus.SCHEDULED) {
            this.status = AppointmentStatus.CONFIRMED;
        }
    }

    public void start() {
        if (status == AppointmentStatus.CONFIRMED) {
            this.status = AppointmentStatus.IN_PROGRESS;
        }
    }

    public void complete() {
        if (status == AppointmentStatus.IN_PROGRESS) {
            this.status = AppointmentStatus.COMPLETED;
        }
    }

    public void cancel() {
        if (status != AppointmentStatus.COMPLETED) {
            this.status = AppointmentStatus.CANCELLED;
        }
    }
}