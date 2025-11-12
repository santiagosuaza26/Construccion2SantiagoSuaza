package app.clinic.infrastructure.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.mapper.AppointmentMapper;
import app.clinic.application.usecase.ScheduleAppointmentUseCase;
import app.clinic.domain.model.valueobject.AppointmentStatus;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.service.PatientService;
import app.clinic.domain.service.UserService;
import app.clinic.infrastructure.dto.AppointmentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "API para gestión de citas médicas")
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

    private Role getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            String roleString = authentication.getAuthorities().iterator().next().getAuthority();
            if (roleString.startsWith("ROLE_")) {
                roleString = roleString.substring(5); // Remove "ROLE_" prefix
            }
            return Role.valueOf(roleString);
        }
        return null;
    }

    @PostMapping
    @Operation(summary = "Programar cita médica", description = "Programa una nueva cita médica para un paciente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita programada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<AppointmentDTO> scheduleAppointment(@Valid @RequestBody ScheduleAppointmentRequest request) {
        var appointment = scheduleAppointmentUseCase.execute(
            request.patientId, request.adminId, request.doctorId,
            LocalDateTime.parse(request.dateTime), request.reason
        );

        // Get patient and user details
        Role currentRole = getCurrentUserRole();
        if (currentRole == null) {
            return ResponseEntity.status(403).build(); // Forbidden
        }
        var patient = patientService.findPatientById(request.patientId, currentRole);
        var admin = userService.findUserById(request.adminId);
        var doctor = userService.findUserById(request.doctorId);

        var dto = AppointmentMapper.toDTO(
            appointment,
            patient.getFullName(),
            request.adminId,
            admin.getFullName(),
            doctor.getFullName(),
            AppointmentStatus.SCHEDULED.name()
        );

        return ResponseEntity.ok(dto);
    }

    @Schema(description = "Solicitud para programar una cita médica")
    public static class ScheduleAppointmentRequest {
        @NotBlank(message = "El ID del paciente es obligatorio")
        @Schema(description = "ID del paciente", example = "12345")
        public String patientId;

        @NotBlank(message = "El ID del administrador es obligatorio")
        @Schema(description = "ID del administrador que programa la cita", example = "admin001")
        public String adminId;

        @NotBlank(message = "El ID del médico es obligatorio")
        @Schema(description = "ID del médico asignado", example = "doc001")
        public String doctorId;

        @NotBlank(message = "La fecha y hora son obligatorias")
        @Schema(description = "Fecha y hora de la cita en formato ISO 8601", example = "2023-12-01T10:00:00")
        public String dateTime;

        @NotBlank(message = "El motivo de la cita es obligatorio")
        @Schema(description = "Motivo de la cita médica", example = "Consulta general")
        public String reason;
    }
}