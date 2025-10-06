package app.clinic.application.dto.appointment;

import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for updating existing appointments.
 * All fields are optional for partial updates.
 */
public class UpdateAppointmentDTO {

    @Size(max = 200, message = "Appointment reason must not exceed 200 characters")
    private String reason;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    // Default constructor
    public UpdateAppointmentDTO() {}

    // Constructor with parameters
    public UpdateAppointmentDTO(String reason, String notes) {
        this.reason = reason;
        this.notes = notes;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return String.format("UpdateAppointmentDTO{reason='%s', notes='%s'}",
                           reason, notes);
    }
}