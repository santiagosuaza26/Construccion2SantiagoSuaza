package app.clinic.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.clinic.infrastructure.entity.MedicalRecordEntity;

/**
 * JPA repository interface for MedicalRecord entity operations.
 * Provides basic CRUD operations and custom queries for medical records.
 */
@Repository
public interface MedicalRecordJpaRepository extends JpaRepository<MedicalRecordEntity, Long> {

    /**
     * Finds a medical record by patient cedula.
     */
    Optional<MedicalRecordEntity> findByPatientCedula(String patientCedula);

    /**
     * Checks if a medical record exists for the given patient cedula.
     */
    boolean existsByPatientCedula(String patientCedula);

    /**
     * Deletes a medical record by patient cedula.
     */
    void deleteByPatientCedula(String patientCedula);

    /**
     * Counts medical records by patient cedula.
     */
    long countByPatientCedula(String patientCedula);

    /**
     * Finds all medical records.
     */
    List<MedicalRecordEntity> findAll();
}