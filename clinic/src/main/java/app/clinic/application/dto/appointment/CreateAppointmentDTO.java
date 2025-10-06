package app.clinic.application.dto.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for creating new appointments.
 * Contains validation annotations for input data.
 */
public class CreateAppointmentDTO {

    @NotBlank(message = "Patient cedula is required")
    @Size(max = 20, message = "Patient cedula must not exceed 20 characters")
    private String patientCedula;

    @NotBlank(message = "Doctor cedula is required")
    @Size(max = 10, message = "Doctor cedula must not exceed 10 characters")
    private String doctorCedula;

    @NotBlank(message = "Appointment date time is required")
    private String appointmentDateTime;

    @Size(max = 200, message = "Appointment reason must not exceed 200 characters")
    private String reason;

    // Default constructor
    public CreateAppointmentDTO() {}

    // Constructor with parameters
    public CreateAppointmentDTO(String patientCedula, String doctorCedula,
                               String appointmentDateTime, String reason) {
        this.patientCedula = patientCedula;
        this.doctorCedula = doctorCedula;
        this.appointmentDateTime = appointmentDateTime;
        this.reason = reason;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return String.format("CreateAppointmentDTO{patientCedula='%s', doctorCedula='%s', appointmentDateTime='%s'}",
                           patientCedula, doctorCedula, appointmentDateTime);
    }
}