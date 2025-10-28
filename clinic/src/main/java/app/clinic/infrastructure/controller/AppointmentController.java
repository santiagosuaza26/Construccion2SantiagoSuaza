package app.clinic.infrastructure.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.ScheduleAppointmentUseCase;
import app.clinic.infrastructure.dto.AppointmentDTO;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final ScheduleAppointmentUseCase scheduleAppointmentUseCase;

    public AppointmentController(ScheduleAppointmentUseCase scheduleAppointmentUseCase) {
        this.scheduleAppointmentUseCase = scheduleAppointmentUseCase;
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> scheduleAppointment(@RequestBody ScheduleAppointmentRequest request) {
        var appointment = scheduleAppointmentUseCase.execute(
            request.patientId, request.adminId, request.doctorId,
            LocalDateTime.parse(request.dateTime), request.reason
        );

        var dto = new AppointmentDTO(
            "appointment-id", // TODO: Generate proper ID
            appointment.getPatientId().getValue(),
            "Patient Name", // TODO: Get from patient service
            "admin-id", // TODO: Get from request
            "Admin Name", // TODO: Get from user service
            appointment.getDoctorId().getValue(),
            "Doctor Name", // TODO: Get from user service
            appointment.getDateTime(),
            appointment.getReason(),
            "SCHEDULED" // TODO: Add status enum
        );

        return ResponseEntity.ok(dto);
    }

    public static class ScheduleAppointmentRequest {
        public String patientId;
        public String adminId;
        public String doctorId;
        public String dateTime;
        public String reason;
    }
}