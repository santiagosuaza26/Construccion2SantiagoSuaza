package app.clinic.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.Appointment;
import app.clinic.domain.model.AppointmentDateTime;
import app.clinic.domain.model.AppointmentId;
import app.clinic.domain.model.AppointmentStatus;
import app.clinic.domain.model.DoctorCedula;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.port.AppointmentRepository;

/**
 * Domain service for appointment operations.
 * Contains business logic for appointment management following domain-driven design principles.
 */
@Service
public class AppointmentDomainService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentDomainService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Schedules a new appointment with validation.
     */
    public Appointment scheduleAppointment(Appointment appointment) {
        validateAppointmentForScheduling(appointment);
        return appointmentRepository.save(appointment);
    }

    /**
     * Updates an existing appointment with validation.
     */
    public Appointment updateAppointment(Appointment appointment) {
        validateAppointmentForUpdate(appointment);
        return appointmentRepository.save(appointment);
    }

    /**
     * Cancels an appointment.
     */
    public Appointment cancelAppointment(AppointmentId appointmentId) {
        Optional<Appointment> existingAppointment = appointmentRepository.findById(appointmentId);
        if (existingAppointment.isEmpty()) {
            throw new IllegalArgumentException("Appointment to cancel does not exist");
        }

        Appointment cancelledAppointment = Appointment.of(
            existingAppointment.get().getId(),
            existingAppointment.get().getPatientCedula(),
            existingAppointment.get().getDoctorCedula(),
            existingAppointment.get().getAppointmentDateTime(),
            AppointmentStatus.CANCELADA,
            existingAppointment.get().getReason()
        );

        return appointmentRepository.save(cancelledAppointment);
    }

    /**
     * Confirms an appointment.
     */
    public Appointment confirmAppointment(AppointmentId appointmentId) {
        Optional<Appointment> existingAppointment = appointmentRepository.findById(appointmentId);
        if (existingAppointment.isEmpty()) {
            throw new IllegalArgumentException("Appointment to confirm does not exist");
        }

        Appointment confirmedAppointment = Appointment.of(
            existingAppointment.get().getId(),
            existingAppointment.get().getPatientCedula(),
            existingAppointment.get().getDoctorCedula(),
            existingAppointment.get().getAppointmentDateTime(),
            AppointmentStatus.CONFIRMADA,
            existingAppointment.get().getReason()
        );

        return appointmentRepository.save(confirmedAppointment);
    }

    /**
     * Finds an appointment by ID.
     */
    public Optional<Appointment> findAppointmentById(AppointmentId appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }

    /**
     * Finds all appointments for a specific patient.
     */
    public List<Appointment> findAppointmentsByPatient(PatientCedula patientCedula) {
        return appointmentRepository.findByPatientCedula(patientCedula);
    }

    /**
     * Finds all appointments for a specific doctor.
     */
    public List<Appointment> findAppointmentsByDoctor(DoctorCedula doctorCedula) {
        return appointmentRepository.findByDoctorCedula(doctorCedula);
    }

    /**
     * Finds all appointments.
     */
    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }

    /**
     * Deletes an appointment by ID.
     */
    public void deleteAppointmentById(AppointmentId appointmentId) {
        validateAppointmentCanBeDeleted(appointmentId);
        appointmentRepository.deleteById(appointmentId);
    }

    /**
     * Validates appointment data for scheduling.
     */
    private void validateAppointmentForScheduling(Appointment appointment) {
        validateAppointmentDateTime(appointment.getAppointmentDateTime());
        validateDoctorAvailability(appointment.getDoctorCedula(), appointment.getAppointmentDateTime());
        // Add additional validation rules
    }

    /**
     * Validates appointment data for update.
     */
    private void validateAppointmentForUpdate(Appointment appointment) {
        Optional<Appointment> existingAppointment = appointmentRepository.findById(AppointmentId.of(appointment.getId()));
        if (existingAppointment.isEmpty()) {
            throw new IllegalArgumentException("Appointment to update does not exist");
        }
        validateAppointmentDateTime(appointment.getAppointmentDateTime());
        // Add additional validation rules
    }

    /**
     * Validates that the appointment can be deleted.
     */
    private void validateAppointmentCanBeDeleted(AppointmentId appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("Appointment to delete does not exist");
        }
        // Add business rules for appointment deletion (e.g., cannot delete past appointments)
    }

    /**
     * Validates appointment date and time.
     */
    private void validateAppointmentDateTime(AppointmentDateTime appointmentDateTime) {
        if (appointmentDateTime.isInThePast()) {
            throw new IllegalArgumentException("Appointment cannot be scheduled in the past");
        }
    }

    /**
     * Validates doctor availability for the appointment time.
     */
    private void validateDoctorAvailability(DoctorCedula doctorCedula, AppointmentDateTime appointmentDateTime) {
        List<Appointment> doctorAppointments = appointmentRepository.findByDoctorCedula(doctorCedula);
        boolean isConflict = doctorAppointments.stream()
            .filter(apt -> apt.getStatus() == AppointmentStatus.PROGRAMADA ||
                          apt.getStatus() == AppointmentStatus.CONFIRMADA)
            .anyMatch(apt -> apt.getAppointmentDateTime().getValue().equals(appointmentDateTime.getValue()));

        if (isConflict) {
            throw new IllegalArgumentException("Doctor is not available at the requested time");
        }
    }
}