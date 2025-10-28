package app.clinic.domain.service;

import java.time.LocalDateTime;

import app.clinic.domain.model.entities.Appointment;
import app.clinic.domain.model.entities.User;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.repository.AppointmentRepository;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;

public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final RoleBasedAccessService roleBasedAccessService;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository, UserRepository userRepository, RoleBasedAccessService roleBasedAccessService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public Appointment scheduleAppointment(String patientId, String adminId, String doctorId, LocalDateTime dateTime, String reason) {
        validateAdminRole(adminId);
        Id patientIdObj = new Id(patientId);
        if (!patientRepository.existsByIdentificationNumber(patientIdObj)) {
            throw new IllegalArgumentException("Patient not found");
        }
        Id doctorIdObj = new Id(doctorId);
        User doctor = userRepository.findByIdentificationNumber(doctorIdObj).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        if (doctor.getRole() != Role.MEDICO) {
            throw new IllegalArgumentException("User is not a doctor");
        }
        if (appointmentRepository.existsByPatientIdAndDateTime(patientIdObj, dateTime)) {
            throw new IllegalArgumentException("Patient already has an appointment at this time");
        }
        Appointment appointment = new Appointment(patientIdObj, doctorIdObj, dateTime, reason);
        appointmentRepository.save(appointment);
        return appointment;
    }

    private void validateAdminRole(String adminId) {
        Id adminIdObj = new Id(adminId);
        User admin = userRepository.findByIdentificationNumber(adminIdObj).orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        roleBasedAccessService.checkAccess(admin.getRole(), "appointment");
    }
}