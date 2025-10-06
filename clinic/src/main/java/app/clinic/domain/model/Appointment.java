package app.clinic.domain.model;

import java.util.Objects;

/**
 * Entity representing a medical appointment.
 * Contains appointment details, patient, doctor, and status information.
 */
public class Appointment {
    private final String id;
    private final PatientCedula patientCedula;
    private final DoctorCedula doctorCedula;
    private final AppointmentDateTime appointmentDateTime;
    private final AppointmentStatus status;
    private final ConsultationReason reason;

    private Appointment(String id, PatientCedula patientCedula, DoctorCedula doctorCedula,
                       AppointmentDateTime appointmentDateTime, AppointmentStatus status,
                       ConsultationReason reason) {
        this.id = id;
        this.patientCedula = patientCedula;
        this.doctorCedula = doctorCedula;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status != null ? status : AppointmentStatus.PROGRAMADA;
        this.reason = reason;
    }

    public static Appointment of(String id, PatientCedula patientCedula, DoctorCedula doctorCedula,
                                AppointmentDateTime appointmentDateTime, AppointmentStatus status,
                                ConsultationReason reason) {
        return new Appointment(id, patientCedula, doctorCedula, appointmentDateTime, status, reason);
    }

    public String getId() {
        return id;
    }

    public PatientCedula getPatientCedula() {
        return patientCedula;
    }

    public DoctorCedula getDoctorCedula() {
        return doctorCedula;
    }

    public AppointmentDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public ConsultationReason getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(patientCedula, that.patientCedula) &&
               Objects.equals(doctorCedula, that.doctorCedula) &&
               Objects.equals(appointmentDateTime, that.appointmentDateTime) &&
               status == that.status &&
               Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientCedula, doctorCedula, appointmentDateTime, status, reason);
    }

    @Override
    public String toString() {
        return String.format("Appointment{id=%s, patient=%s, doctor=%s, dateTime=%s, status=%s}",
                           id, patientCedula, doctorCedula, appointmentDateTime, status);
    }
}