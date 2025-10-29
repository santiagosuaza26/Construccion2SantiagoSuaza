package app.clinic.infrastructure.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.ScheduleAppointmentUseCase;
import app.clinic.domain.model.valueobject.AppointmentStatus;
import app.clinic.domain.service.PatientService;
import app.clinic.domain.service.UserService;
import app.clinic.infrastructure.dto.AppointmentDTO;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final ScheduleAppointmentUseCase scheduleAppointmentUseCase;
    private final PatientService patientService;
    private final UserService userService;

    public AppointmentController(ScheduleAppointmentUseCase scheduleAppointmentUseCase,
                                PatientService patientService,
                                UserService userService) {
        this.scheduleAppointmentUseCase = scheduleAppointmentUseCase;
        this.patientService = patientService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> scheduleAppointment(@RequestBody ScheduleAppointmentRequest request) {
        var appointment = scheduleAppointmentUseCase.execute(
            request.patientId, request.adminId, request.doctorId,
            LocalDateTime.parse(request.dateTime), request.reason
        );

        // Get patient and user details
        var patient = patientService.findPatientById(request.patientId);
        var admin = userService.findUserById(request.adminId);
        var doctor = userService.findUserById(request.doctorId);

        var dto = new AppointmentDTO(
            appointment.getPatientId().getValue(), // Use patient ID as appointment ID
            appointment.getPatientId().getValue(),
            patient.getFullName(), // Get from patient service
            request.adminId, // Get from request
            admin.getFullName(), // Get from user service
            appointment.getDoctorId().getValue(),
            doctor.getFullName(), // Get from user service
            appointment.getDateTime(),
            appointment.getReason(),
            AppointmentStatus.SCHEDULED.name() // Use status enum
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