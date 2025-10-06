package app.clinic.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.dto.appointment.CreateAppointmentDTO;
import app.clinic.application.dto.appointment.AppointmentDTO;
import app.clinic.application.dto.appointment.UpdateAppointmentDTO;
import app.clinic.application.service.AppointmentApplicationService;
import jakarta.validation.Valid;

/**
 * REST Controller for appointment management operations.
 * Provides HTTP endpoints for scheduling, managing, and tracking appointments.
 * Only accessible by administrative staff and doctors.
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentApplicationService appointmentApplicationService;

    public AppointmentController(AppointmentApplicationService appointmentApplicationService) {
        this.appointmentApplicationService = appointmentApplicationService;
    }

    /**
     * Schedules a new appointment.
     * Only accessible by Administrative Staff.
     */
    @PostMapping
    public ResponseEntity<AppointmentDTO> scheduleAppointment(@Valid @RequestBody CreateAppointmentDTO createAppointmentDTO) {
        AppointmentDTO scheduledAppointment = appointmentApplicationService.scheduleAppointment(createAppointmentDTO);
        return new ResponseEntity<>(scheduledAppointment, HttpStatus.CREATED);
    }

    /**
     * Updates an existing appointment.
     * Only accessible by Administrative Staff and Doctors.
     */
    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable String appointmentId,
                                                           @Valid @RequestBody UpdateAppointmentDTO updateAppointmentDTO) {
        AppointmentDTO updatedAppointment = appointmentApplicationService.updateAppointment(appointmentId, updateAppointmentDTO);
        return ResponseEntity.ok(updatedAppointment);
    }

    /**
     * Finds an appointment by ID.
     */
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDTO> findAppointmentById(@PathVariable String appointmentId) {
        Optional<AppointmentDTO> appointment = appointmentApplicationService.findAppointmentById(appointmentId);
        return appointment.map(a -> ResponseEntity.ok(a))
                         .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Finds all appointments for a specific patient.
     */
    @GetMapping("/patient/{patientCedula}")
    public ResponseEntity<List<AppointmentDTO>> findAppointmentsByPatientCedula(@PathVariable String patientCedula) {
        List<AppointmentDTO> appointments = appointmentApplicationService.findAppointmentsByPatientCedula(patientCedula);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Finds all appointments for a specific doctor.
     */
    @GetMapping("/doctor/{doctorCedula}")
    public ResponseEntity<List<AppointmentDTO>> findAppointmentsByDoctorCedula(@PathVariable String doctorCedula) {
        List<AppointmentDTO> appointments = appointmentApplicationService.findAppointmentsByDoctorCedula(doctorCedula);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Finds all appointments within a date range.
     */
    @GetMapping("/daterange/{startDate}/{endDate}")
    public ResponseEntity<List<AppointmentDTO>> findAppointmentsByDateRange(@PathVariable String startDate,
                                                                           @PathVariable String endDate) {
        List<AppointmentDTO> appointments = appointmentApplicationService.findAppointmentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Cancels an appointment.
     * Only accessible by Administrative Staff and Doctors.
     */
    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<AppointmentDTO> cancelAppointment(@PathVariable String appointmentId) {
        AppointmentDTO cancelledAppointment = appointmentApplicationService.cancelAppointment(appointmentId);
        return ResponseEntity.ok(cancelledAppointment);
    }

    /**
     * Marks an appointment as completed.
     * Only accessible by Doctors.
     */
    @PutMapping("/{appointmentId}/complete")
    public ResponseEntity<AppointmentDTO> completeAppointment(@PathVariable String appointmentId) {
        AppointmentDTO completedAppointment = appointmentApplicationService.completeAppointment(appointmentId);
        return ResponseEntity.ok(completedAppointment);
    }

    /**
     * Deletes an appointment by ID.
     * Only accessible by Administrative Staff.
     */
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable String appointmentId) {
        appointmentApplicationService.deleteAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Checks if a doctor is available at a specific date and time.
     */
    @GetMapping("/doctor/{doctorCedula}/availability/{dateTime}")
    public ResponseEntity<Boolean> checkDoctorAvailability(@PathVariable String doctorCedula,
                                                          @PathVariable String dateTime) {
        boolean available = appointmentApplicationService.checkDoctorAvailability(doctorCedula, dateTime);
        return ResponseEntity.ok(available);
    }

    /**
     * Gets appointment statistics for a patient.
     */
    @GetMapping("/patient/{patientCedula}/statistics")
    public ResponseEntity<AppointmentApplicationService.AppointmentStatisticsDTO> getAppointmentStatistics(@PathVariable String patientCedula) {
        AppointmentApplicationService.AppointmentStatisticsDTO statistics = appointmentApplicationService.getAppointmentStatistics(patientCedula);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Checks if a patient has any upcoming appointments.
     */
    @GetMapping("/patient/{patientCedula}/upcoming")
    public ResponseEntity<Boolean> hasUpcomingAppointments(@PathVariable String patientCedula) {
        boolean hasUpcoming = appointmentApplicationService.hasUpcomingAppointments(patientCedula);
        return ResponseEntity.ok(hasUpcoming);
    }
}