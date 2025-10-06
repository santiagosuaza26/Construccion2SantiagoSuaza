package app.clinic.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.clinic.infrastructure.entity.PatientEntity;

/**
 * JPA repository interface for Patient entity operations.
 * Provides basic CRUD operations and custom queries for patient management.
 */
@Repository
public interface PatientJpaRepository extends JpaRepository<PatientEntity, Long> {

    /**
     * Finds a patient by their cedula.
     */
    Optional<PatientEntity> findByCedula(String cedula);

    /**
     * Finds a patient by their username.
     */
    Optional<PatientEntity> findByUsername(String username);

    /**
     * Checks if a patient exists with the given cedula.
     */
    boolean existsByCedula(String cedula);

    /**
     * Checks if a patient exists with the given username.
     */
    boolean existsByUsername(String username);

    /**
     * Finds patients by gender.
     */
    List<PatientEntity> findByGender(PatientEntity.PatientGender gender);

    /**
     * Finds patients with active insurance policies.
     */
    @Query("SELECT p FROM PatientEntity p WHERE p.insurancePolicy IS NOT NULL AND p.insurancePolicy.status = 'ACTIVE' AND p.insurancePolicy.expirationDate > CURRENT_DATE")
    List<PatientEntity> findPatientsWithActiveInsurance();

    /**
     * Counts patients by gender.
     */
    long countByGender(PatientEntity.PatientGender gender);
}