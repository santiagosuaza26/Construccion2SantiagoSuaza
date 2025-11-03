package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.AppointmentService;

@Service
public class CancelAppointmentUseCase {
    private final AppointmentService appointmentService;

    public CancelAppointmentUseCase(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public void execute(String appointmentId, String adminId) {
        appointmentService.cancelAppointment(appointmentId, adminId);
    }
}