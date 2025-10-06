package app.clinic.infrastructure.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.clinic.domain.model.Appointment;
import app.clinic.domain.model.AppointmentId;
import app.clinic.domain.model.DoctorCedula;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.port.AppointmentRepository;

/**
 * Infrastructure service for appointment management operations.
 * Provides business logic implementation for appointment-related operations.
 */
@Service
@Transactional
public class AppointmentManagementInfrastructureService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentManagementInfrastructureService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Creates a new appointment.
     */
    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    /**
     * Updates an existing appointment.
     */
    public Appointment updateAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    /**
     * Finds an appointment by ID.
     */
    @Transactional(readOnly = true)
    public Optional<Appointment> findAppointmentById(AppointmentId appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }

    /**
     * Finds all appointments for a specific patient.
     */
    @Transactional(readOnly = true)
    public List<Appointment> findAppointmentsByPatient(PatientCedula patientCedula) {
        return appointmentRepository.findByPatientCedula(patientCedula);
    }

    /**
     * Finds all appointments for a specific doctor.
     */
    @Transactional(readOnly = true)
    public List<Appointment> findAppointmentsByDoctor(DoctorCedula doctorCedula) {
        return appointmentRepository.findByDoctorCedula(doctorCedula);
    }

    /**
     * Finds all appointments.
     */
    @Transactional(readOnly = true)
    public List<Appointment> findAllAppointments() {
        return appointmentRepository.findAll();
    }

    /**
     * Deletes an appointment by ID.
     */
    public void deleteAppointmentById(AppointmentId appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }

    /**
     * Checks if an appointment exists with the given ID.
     */
    @Transactional(readOnly = true)
    public boolean appointmentExistsById(AppointmentId appointmentId) {
        return appointmentRepository.existsById(appointmentId);
    }

    /**
     * Counts total number of appointments.
     */
    @Transactional(readOnly = true)
    public long countAppointments() {
        return appointmentRepository.count();
    }

    /**
     * Counts appointments by patient.
     */
    @Transactional(readOnly = true)
    public long countAppointmentsByPatient(PatientCedula patientCedula) {
        return appointmentRepository.countByPatient(patientCedula);
    }

    /**
     * Counts appointments by doctor.
     */
    @Transactional(readOnly = true)
    public long countAppointmentsByDoctor(DoctorCedula doctorCedula) {
        return appointmentRepository.countByDoctor(doctorCedula);
    }
}