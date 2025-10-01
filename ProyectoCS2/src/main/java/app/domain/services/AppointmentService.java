package app.domain.services;

import java.time.LocalDateTime;
import java.util.List;

import app.domain.exception.DomainValidationException;
import app.domain.model.Appointment;
import app.domain.model.Patient;
import app.domain.model.User;
import app.domain.port.AppointmentRepository;
import app.domain.port.PatientRepository;
import app.domain.port.UserRepository;
import app.domain.services.AuthenticationService.AuthenticatedUser;

/**
 * Servicio especializado para gestión de citas médicas
 * Sigue principios SOLID:
 * - SRP: Una sola responsabilidad (gestionar citas médicas)
 * - DIP: Depende de abstracciones (repositorios)
 */
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                             PatientRepository patientRepository,
                             UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    /**
     * Programa una nueva cita médica
     */
    public Appointment scheduleAppointment(String patientIdCard, String doctorIdCard,
                                          LocalDateTime appointmentDateTime, String appointmentType,
                                          String notes, AuthenticatedUser scheduledBy) {
        // Validar que el paciente existe
        if (!patientRepository.existsByIdCard(patientIdCard)) {
            throw new DomainValidationException("Patient with ID card " + patientIdCard + " does not exist");
        }

        // Validar que el doctor existe y tiene rol médico
        User doctor = userRepository.findByIdCard(doctorIdCard)
            .orElseThrow(() -> new DomainValidationException("Doctor with ID card " + doctorIdCard + " does not exist"));

        if (doctor.getRole() != app.domain.model.Role.DOCTOR) {
            throw new DomainValidationException("User " + doctorIdCard + " is not a doctor");
        }

        // Validar que la fecha de la cita es futura
        if (!appointmentDateTime.isAfter(LocalDateTime.now())) {
            throw new DomainValidationException("Appointment date must be in the future");
        }

        // Validar que el doctor no tenga conflicto de horario
        if (appointmentRepository.existsByDoctorAndDateTime(doctorIdCard, appointmentDateTime)) {
            throw new DomainValidationException("Doctor already has an appointment at this time");
        }

        // Crear la cita
        String appointmentId = generateAppointmentId();
        Appointment appointment = new Appointment(
            appointmentId,
            patientIdCard,
            doctorIdCard,
            appointmentDateTime,
            appointmentType,
            "PENDING",
            notes,
            scheduledBy.getIdCard()
        );

        return appointmentRepository.save(appointment);
    }

    /**
     * Confirma una cita médica
     */
    public Appointment confirmAppointment(String appointmentId, AuthenticatedUser confirmedBy) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new DomainValidationException("Appointment " + appointmentId + " does not exist"));

        if (!appointment.isFutureAppointment()) {
            throw new DomainValidationException("Cannot confirm past appointment");
        }

        // Crear nueva instancia con estado confirmado (en lugar de modificar)
        Appointment confirmedAppointment = new Appointment(
            appointment.getId(),
            appointment.getPatientIdCard(),
            appointment.getDoctorIdCard(),
            appointment.getAppointmentDateTime(),
            appointment.getAppointmentType(),
            "CONFIRMED",
            appointment.getNotes(),
            confirmedBy.getIdCard()
        );

        return appointmentRepository.save(confirmedAppointment);
    }

    /**
     * Cancela una cita médica
     */
    public Appointment cancelAppointment(String appointmentId, String reason, AuthenticatedUser cancelledBy) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new DomainValidationException("Appointment " + appointmentId + " does not exist"));

        if (!appointment.canBeCancelled()) {
            throw new DomainValidationException("Appointment cannot be cancelled");
        }

        // Crear nueva instancia con estado cancelado
        Appointment cancelledAppointment = new Appointment(
            appointment.getId(),
            appointment.getPatientIdCard(),
            appointment.getDoctorIdCard(),
            appointment.getAppointmentDateTime(),
            appointment.getAppointmentType(),
            "CANCELLED",
            reason != null ? reason : appointment.getNotes(),
            cancelledBy.getIdCard()
        );

        return appointmentRepository.save(cancelledAppointment);
    }

    /**
     * Obtiene todas las citas de un paciente
     */
    public List<Appointment> getPatientAppointments(String patientIdCard) {
        if (!patientRepository.existsByIdCard(patientIdCard)) {
            throw new DomainValidationException("Patient with ID card " + patientIdCard + " does not exist");
        }

        return appointmentRepository.findByPatientIdCard(patientIdCard);
    }

    /**
     * Obtiene todas las citas de un doctor
     */
    public List<Appointment> getDoctorAppointments(String doctorIdCard) {
        if (!userRepository.existsByIdCard(doctorIdCard)) {
            throw new DomainValidationException("Doctor with ID card " + doctorIdCard + " does not exist");
        }

        return appointmentRepository.findByDoctorIdCard(doctorIdCard);
    }

    /**
     * Obtiene citas por fecha
     */
    public List<Appointment> getAppointmentsByDate(LocalDateTime date) {
        return appointmentRepository.findByDate(date.toLocalDate());
    }

    /**
     * Valida que un usuario pueda programar citas
     */
    public void validateCanScheduleAppointments(AuthenticatedUser user) {
        if (!user.canRegisterPatients() && user.getRole() != app.domain.model.Role.DOCTOR) {
            throw new DomainValidationException("User " + user.getIdCard() + " is not authorized to schedule appointments");
        }
    }

    /**
     * Valida que un usuario pueda acceder a información de citas
     */
    public void validateCanAccessAppointment(AuthenticatedUser user, Appointment appointment) {
        // El paciente puede ver sus propias citas
        if (user.getRole() == app.domain.model.Role.PATIENT &&
            user.getIdCard().equals(appointment.getPatientIdCard())) {
            return;
        }

        // El doctor puede ver sus propias citas
        if (user.getRole() == app.domain.model.Role.DOCTOR &&
            user.getIdCard().equals(appointment.getDoctorIdCard())) {
            return;
        }

        // Personal administrativo puede ver todas las citas
        if (user.canRegisterPatients()) {
            return;
        }

        throw new DomainValidationException("User " + user.getIdCard() + " is not authorized to access this appointment");
    }

    /**
     * Genera un ID único para la cita
     */
    private String generateAppointmentId() {
        return "APT" + System.currentTimeMillis();
    }
}