package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.application.usecase.ScheduleAppointmentUseCase;
import app.clinic.domain.service.AppointmentService;

@Service
public class ScheduleAppointmentUseCaseService extends ScheduleAppointmentUseCase {

    public ScheduleAppointmentUseCaseService(AppointmentService appointmentService) {
        super(appointmentService);
    }

    // Infrastructure layer service that extends the domain use case
    // Can add infrastructure-specific concerns like logging, caching, etc.
}