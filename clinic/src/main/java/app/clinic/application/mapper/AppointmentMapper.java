package app.clinic.application.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import app.clinic.application.dto.appointment.AppointmentDTO;
import app.clinic.application.dto.appointment.CreateAppointmentDTO;
import app.clinic.application.dto.appointment.UpdateAppointmentDTO;
import app.clinic.domain.model.Appointment;
import app.clinic.domain.model.AppointmentDateTime;
import app.clinic.domain.model.AppointmentStatus;
import app.clinic.domain.model.ConsultationReason;
import app.clinic.domain.model.DoctorCedula;
import app.clinic.domain.model.PatientCedula;

/**
 * Mapper class for converting between Appointment DTOs and domain entities.
 */
public class AppointmentMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Converts a CreateAppointmentDTO to domain entity.
     */
    public static Appointment toDomainEntity(CreateAppointmentDTO createAppointmentDTO) {
        String appointmentId = UUID.randomUUID().toString();

        return Appointment.of(
            appointmentId,
            PatientCedula.of(createAppointmentDTO.getPatientCedula()),
            DoctorCedula.of(createAppointmentDTO.getDoctorCedula()),
            AppointmentDateTime.of(LocalDateTime.parse(createAppointmentDTO.getAppointmentDateTime(), DATE_TIME_FORMATTER)),
            AppointmentStatus.PROGRAMADA,
            createAppointmentDTO.getReason() != null ? ConsultationReason.of(createAppointmentDTO.getReason()) : null
        );
    }

    /**
     * Converts a domain entity to DTO.
     */
    public static AppointmentDTO toDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setPatientCedula(appointment.getPatientCedula().getValue());
        dto.setDoctorCedula(appointment.getDoctorCedula().getValue());
        dto.setAppointmentDateTime(appointment.getAppointmentDateTime().getValue().format(DATE_TIME_FORMATTER));
        dto.setStatus(appointment.getStatus().getDisplayName());
        dto.setReason(appointment.getReason() != null ? appointment.getReason().getValue() : null);
        dto.setNotes(null); // Notes field not available in domain entity
        return dto;
    }

    /**
     * Converts a list of domain entities to DTOs.
     */
    public static List<AppointmentDTO> toDTOList(List<Appointment> appointments) {
        return appointments.stream()
                .map(AppointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing appointment with data from UpdateAppointmentDTO.
     */
    public static Appointment updateFromDTO(Appointment existingAppointment, UpdateAppointmentDTO updateDTO) {
        ConsultationReason reason = updateDTO.getReason() != null ?
            ConsultationReason.of(updateDTO.getReason()) : existingAppointment.getReason();

        return Appointment.of(
            existingAppointment.getId(),
            existingAppointment.getPatientCedula(),
            existingAppointment.getDoctorCedula(),
            existingAppointment.getAppointmentDateTime(),
            existingAppointment.getStatus(),
            reason
        );
    }
}