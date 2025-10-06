package app.clinic.application.dto.appointment;

/**
 * Data Transfer Object for appointment information.
 * Used for API responses containing appointment data.
 */
public class AppointmentDTO {
    private String id;
    private String patientCedula;
    private String doctorCedula;
    private String appointmentDateTime;
    private String reason;
    private String status;
    private String notes;

    // Default constructor
    public AppointmentDTO() {}

    // Constructor with parameters
    public AppointmentDTO(String id, String patientCedula, String doctorCedula,
                         String appointmentDateTime, String reason, String status, String notes) {
        this.id = id;
        this.patientCedula = patientCedula;
        this.doctorCedula = doctorCedula;
        this.appointmentDateTime = appointmentDateTime;
        this.reason = reason;
        this.status = status;
        this.notes = notes;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(String appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return String.format("AppointmentDTO{id='%s', patientCedula='%s', doctorCedula='%s', status='%s'}",
                           id, patientCedula, doctorCedula, status);
    }
}