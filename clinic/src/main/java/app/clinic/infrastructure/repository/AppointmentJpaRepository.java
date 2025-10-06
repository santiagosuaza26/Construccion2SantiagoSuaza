package app.clinic.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.clinic.infrastructure.entity.AppointmentEntity;

/**
 * JPA repository interface for Appointment entity operations.
 * Provides basic CRUD operations and custom queries for appointment management.
 */
@Repository
public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Long> {

    /**
     * Finds appointments by patient cedula.
     */
    List<AppointmentEntity> findByPatientCedula(String patientCedula);

    /**
     * Finds appointments by doctor cedula.
     */
    List<AppointmentEntity> findByDoctorCedula(String doctorCedula);

    /**
     * Finds appointments by status.
     */
    List<AppointmentEntity> findByStatus(AppointmentEntity.AppointmentStatus status);

    /**
     * Finds appointments by date and time.
     */
    List<AppointmentEntity> findByAppointmentDateTime(LocalDateTime appointmentDateTime);

    /**
     * Checks if an appointment exists with the given ID.
     */
    boolean existsById(Long id);

    /**
     * Counts appointments by status.
     */
    long countByStatus(AppointmentEntity.AppointmentStatus status);

    /**
     * Counts appointments by patient.
     */
    long countByPatientCedula(String patientCedula);

    /**
     * Counts appointments by doctor.
     */
    long countByDoctorCedula(String doctorCedula);
}