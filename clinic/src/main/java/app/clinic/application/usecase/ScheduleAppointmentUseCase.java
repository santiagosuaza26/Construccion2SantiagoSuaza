package app.clinic.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.Appointment;
import app.clinic.domain.service.AppointmentService;

@Service
public class ScheduleAppointmentUseCase {
    private final AppointmentService appointmentService;

    public ScheduleAppointmentUseCase(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public Appointment execute(String patientId, String adminId, String doctorId, LocalDateTime dateTime, String reason) {
        return appointmentService.scheduleAppointment(patientId, adminId, doctorId, dateTime, reason);
    }
}