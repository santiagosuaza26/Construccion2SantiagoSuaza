package app.clinic.application.mapper;

import app.clinic.domain.model.entities.Appointment;
import app.clinic.infrastructure.dto.AppointmentDTO;

public class AppointmentMapper {

    public static AppointmentDTO toDTO(Appointment appointment, String patientName, String adminId, String adminName, String doctorName, String status) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getPatientId().getValue()); // Using patient ID as appointment ID
        dto.setPatientId(appointment.getPatientId().getValue());
        dto.setPatientName(patientName);
        dto.setAdminId(adminId);
        dto.setAdminName(adminName);
        dto.setDoctorId(appointment.getDoctorId().getValue());
        dto.setDoctorName(doctorName);
        dto.setDateTime(appointment.getDateTime());
        dto.setReason(appointment.getReason());
        dto.setStatus(status);
        return dto;
    }
}