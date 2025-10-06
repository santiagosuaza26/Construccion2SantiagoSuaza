package app.clinic.application.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import app.clinic.application.dto.appointment.AppointmentDTO;
import app.clinic.application.dto.appointment.CreateAppointmentDTO;
import app.clinic.application.dto.appointment.UpdateAppointmentDTO;

/**
 * Application service for appointment management operations.
 * Coordinates between REST controllers and domain services.
 * Handles appointment-related use cases and business operations.
 */
@Service
public class AppointmentApplicationService {

    // In-memory storage for appointments (replace with real repository later)
    private final ConcurrentMap<String, AppointmentDTO> appointments = new ConcurrentHashMap<>();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Schedules a new appointment.
     */
    public AppointmentDTO scheduleAppointment(CreateAppointmentDTO createAppointmentDTO) {
        // Generate new appointment ID
        String appointmentId = UUID.randomUUID().toString();

        // Create new appointment DTO
        AppointmentDTO appointment = new AppointmentDTO();
        appointment.setId(appointmentId);
        appointment.setPatientCedula(createAppointmentDTO.getPatientCedula());
        appointment.setDoctorCedula(createAppointmentDTO.getDoctorCedula());
        appointment.setAppointmentDateTime(createAppointmentDTO.getAppointmentDateTime());
        appointment.setReason(createAppointmentDTO.getReason());
        appointment.setStatus("Programada");
        appointment.setNotes("");

        // Store appointment
        appointments.put(appointmentId, appointment);

        return appointment;
    }

    /**
     * Updates an existing appointment.
     */
    public AppointmentDTO updateAppointment(String appointmentId, UpdateAppointmentDTO updateAppointmentDTO) {
        // Find existing appointment
        AppointmentDTO existingAppointment = appointments.get(appointmentId);
        if (existingAppointment == null) {
            throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
        }

        // Update fields if provided
        if (updateAppointmentDTO.getReason() != null) {
            existingAppointment.setReason(updateAppointmentDTO.getReason());
        }
        if (updateAppointmentDTO.getNotes() != null) {
            existingAppointment.setNotes(updateAppointmentDTO.getNotes());
        }

        return existingAppointment;
    }

    /**
     * Finds an appointment by ID.
     */
    public Optional<AppointmentDTO> findAppointmentById(String appointmentId) {
        return Optional.ofNullable(appointments.get(appointmentId));
    }

    /**
     * Finds all appointments for a specific patient.
     */
    public List<AppointmentDTO> findAppointmentsByPatientCedula(String patientCedula) {
        return appointments.values().stream()
                .filter(apt -> apt.getPatientCedula().equals(patientCedula))
                .collect(Collectors.toList());
    }

    /**
     * Finds all appointments for a specific doctor.
     */
    public List<AppointmentDTO> findAppointmentsByDoctorCedula(String doctorCedula) {
        return appointments.values().stream()
                .filter(apt -> apt.getDoctorCedula().equals(doctorCedula))
                .collect(Collectors.toList());
    }

    /**
     * Finds all appointments within a date range.
     */
    public List<AppointmentDTO> findAppointmentsByDateRange(String startDate, String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate, DATE_TIME_FORMATTER);
        LocalDateTime end = LocalDateTime.parse(endDate, DATE_TIME_FORMATTER);

        return appointments.values().stream()
            .filter(apt -> {
                LocalDateTime aptDateTime = LocalDateTime.parse(apt.getAppointmentDateTime(), DATE_TIME_FORMATTER);
                return !aptDateTime.isBefore(start) && !aptDateTime.isAfter(end);
            })
            .collect(Collectors.toList());
    }

    /**
     * Cancels an appointment.
     */
    public AppointmentDTO cancelAppointment(String appointmentId) {
        AppointmentDTO appointment = appointments.get(appointmentId);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
        }

        appointment.setStatus("Cancelada");
        return appointment;
    }

    /**
     * Marks an appointment as completed.
     */
    public AppointmentDTO completeAppointment(String appointmentId) {
        AppointmentDTO appointment = appointments.get(appointmentId);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
        }

        appointment.setStatus("Completada");
        return appointment;
    }

    /**
     * Deletes an appointment by ID.
     */
    public void deleteAppointment(String appointmentId) {
        appointments.remove(appointmentId);
    }

    /**
     * Checks if a doctor is available at a specific date and time.
     */
    public boolean checkDoctorAvailability(String doctorCedula, String dateTime) {
        try {
            LocalDateTime requestedDateTime = LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);

            // Check for conflicting appointments
            return appointments.values().stream()
                .filter(apt -> apt.getDoctorCedula().equals(doctorCedula))
                .filter(apt -> "Programada".equals(apt.getStatus()) || "Confirmada".equals(apt.getStatus()))
                .noneMatch(apt -> {
                    LocalDateTime aptDateTime = LocalDateTime.parse(apt.getAppointmentDateTime(), DATE_TIME_FORMATTER);
                    return aptDateTime.equals(requestedDateTime);
                });
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets appointment statistics for a patient.
     */
    public AppointmentStatisticsDTO getAppointmentStatistics(String patientCedula) {
        List<AppointmentDTO> patientAppointments = findAppointmentsByPatientCedula(patientCedula);

        int totalAppointments = patientAppointments.size();
        int completedAppointments = (int) patientAppointments.stream()
            .filter(apt -> "Completada".equals(apt.getStatus()))
            .count();
        int cancelledAppointments = (int) patientAppointments.stream()
            .filter(apt -> "Cancelada".equals(apt.getStatus()))
            .count();
        int upcomingAppointments = (int) patientAppointments.stream()
            .filter(apt -> "Programada".equals(apt.getStatus()) || "Confirmada".equals(apt.getStatus()))
            .filter(apt -> {
                LocalDateTime aptDateTime = LocalDateTime.parse(apt.getAppointmentDateTime(), DATE_TIME_FORMATTER);
                return aptDateTime.isAfter(LocalDateTime.now());
            })
            .count();

        return new AppointmentStatisticsDTO(totalAppointments, completedAppointments,
                                          cancelledAppointments, upcomingAppointments);
    }

    /**
     * Checks if a patient has any upcoming appointments.
     */
    public boolean hasUpcomingAppointments(String patientCedula) {
        return appointments.values().stream()
            .filter(apt -> apt.getPatientCedula().equals(patientCedula))
            .filter(apt -> "Programada".equals(apt.getStatus()) || "Confirmada".equals(apt.getStatus()))
            .anyMatch(apt -> {
                LocalDateTime aptDateTime = LocalDateTime.parse(apt.getAppointmentDateTime(), DATE_TIME_FORMATTER);
                return aptDateTime.isAfter(LocalDateTime.now());
            });
    }

    /**
     * DTO for appointment statistics.
     */
    public static class AppointmentStatisticsDTO {
        private int totalAppointments;
        private int completedAppointments;
        private int cancelledAppointments;
        private int upcomingAppointments;

        // Constructors, getters and setters
        public AppointmentStatisticsDTO() {}

        public AppointmentStatisticsDTO(int totalAppointments, int completedAppointments,
                                       int cancelledAppointments, int upcomingAppointments) {
            this.totalAppointments = totalAppointments;
            this.completedAppointments = completedAppointments;
            this.cancelledAppointments = cancelledAppointments;
            this.upcomingAppointments = upcomingAppointments;
        }

        // Getters and setters
        public int getTotalAppointments() { return totalAppointments; }
        public void setTotalAppointments(int totalAppointments) { this.totalAppointments = totalAppointments; }
        public int getCompletedAppointments() { return completedAppointments; }
        public void setCompletedAppointments(int completedAppointments) { this.completedAppointments = completedAppointments; }
        public int getCancelledAppointments() { return cancelledAppointments; }
        public void setCancelledAppointments(int cancelledAppointments) { this.cancelledAppointments = cancelledAppointments; }
        public int getUpcomingAppointments() { return upcomingAppointments; }
        public void setUpcomingAppointments(int upcomingAppointments) { this.upcomingAppointments = upcomingAppointments; }
    }
}