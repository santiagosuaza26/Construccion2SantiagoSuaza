package app.clinic.domain.port;

import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.Appointment;
import app.clinic.domain.model.AppointmentDateTime;
import app.clinic.domain.model.AppointmentId;
import app.clinic.domain.model.AppointmentStatus;
import app.clinic.domain.model.DoctorCedula;
import app.clinic.domain.model.PatientCedula;

/**
 * Port interface for appointment repository operations.
 * Defines the contract for appointment data access in the domain layer.
 */
public interface AppointmentRepository {

    /**
     * Saves a new appointment or updates an existing one.
     */
    Appointment save(Appointment appointment);

    /**
     * Finds an appointment by its unique identifier.
     */
    Optional<Appointment> findById(AppointmentId appointmentId);

    /**
     * Finds all appointments for a specific patient.
     */
    List<Appointment> findByPatientCedula(PatientCedula patientCedula);

    /**
     * Finds all appointments for a specific doctor.
     */
    List<Appointment> findByDoctorCedula(DoctorCedula doctorCedula);

    /**
     * Finds appointments by date and time.
     */
    List<Appointment> findByDateTime(AppointmentDateTime appointmentDateTime);

    /**
     * Finds appointments by status.
     */
    List<Appointment> findByStatus(AppointmentStatus status);

    /**
     * Finds all appointments.
     */
    List<Appointment> findAll();

    /**
     * Checks if an appointment exists with the given ID.
     */
    boolean existsById(AppointmentId appointmentId);

    /**
     * Deletes an appointment by its identifier.
     */
    void deleteById(AppointmentId appointmentId);

    /**
     * Counts total number of appointments.
     */
    long count();

    /**
     * Counts appointments by status.
     */
    long countByStatus(AppointmentStatus status);

    /**
     * Counts appointments by patient.
     */
    long countByPatient(PatientCedula patientCedula);

    /**
     * Counts appointments by doctor.
     */
    long countByDoctor(DoctorCedula doctorCedula);
}