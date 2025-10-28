package app.clinic.domain.model.entities;

import java.time.LocalDateTime;

import app.clinic.domain.model.valueobject.Id;

public class Appointment {
    private final Id patientId;
    private final Id doctorId;
    private final LocalDateTime dateTime;
    private final String reason;

    public Appointment(Id patientId, Id doctorId, LocalDateTime dateTime, String reason) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.reason = reason;
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
}