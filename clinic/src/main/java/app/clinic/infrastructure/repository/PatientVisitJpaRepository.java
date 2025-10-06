package app.clinic.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.clinic.infrastructure.entity.PatientVisitEntity;

/**
 * JPA repository interface for PatientVisit entity operations.
 * Provides basic CRUD operations and custom queries for patient visits.
 */
@Repository
public interface PatientVisitJpaRepository extends JpaRepository<PatientVisitEntity, Long> {

    /**
     * Finds a patient visit by its ID.
     */
    Optional<PatientVisitEntity> findById(Long id);

    /**
     * Finds all patient visits for a specific patient.
     */
    List<PatientVisitEntity> findByPatientCedula(String patientCedula);

    /**
     * Finds all patient visits.
     */
    List<PatientVisitEntity> findAll();

    /**
     * Checks if a patient visit exists with the given ID.
     */
    boolean existsById(Long id);

    /**
     * Counts total number of patient visits.
     */
    long count();

    /**
     * Counts patient visits by patient.
     */
    long countByPatientCedula(String patientCedula);

    /**
     * Finds patient visits within a date range.
     */
    List<PatientVisitEntity> findByVisitDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}