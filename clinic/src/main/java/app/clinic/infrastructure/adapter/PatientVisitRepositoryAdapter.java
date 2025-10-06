package app.clinic.infrastructure.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientVisit;
import app.clinic.domain.model.PatientVisitId;
import app.clinic.domain.port.PatientVisitRepository;
import app.clinic.infrastructure.entity.PatientVisitEntity;
import app.clinic.infrastructure.repository.PatientVisitJpaRepository;

/**
 * Adapter that implements the PatientVisitRepository port using JPA.
 * Converts between domain objects and JPA entities for patient visits.
 */
@Repository
public class PatientVisitRepositoryAdapter implements PatientVisitRepository {

    private final PatientVisitJpaRepository patientVisitJpaRepository;

    public PatientVisitRepositoryAdapter(PatientVisitJpaRepository patientVisitJpaRepository) {
        this.patientVisitJpaRepository = patientVisitJpaRepository;
    }

    @Override
    public PatientVisit save(PatientVisit patientVisit) {
        PatientVisitEntity entity = toEntity(patientVisit);
        PatientVisitEntity savedEntity = patientVisitJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<PatientVisit> findById(PatientVisitId patientVisitId) {
        return patientVisitJpaRepository.findById(Long.valueOf(patientVisitId.getValue()))
                .map(this::toDomain);
    }

    @Override
    public List<PatientVisit> findByPatientCedula(PatientCedula patientCedula) {
        return patientVisitJpaRepository.findByPatientCedula(patientCedula.getValue())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientVisit> findAll() {
        return patientVisitJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientVisit> findAllCompleted() {
        return patientVisitJpaRepository.findAll()
                .stream()
                .filter(PatientVisitEntity::isCompleted)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(PatientVisitId patientVisitId) {
        return patientVisitJpaRepository.existsById(Long.valueOf(patientVisitId.getValue()));
    }

    @Override
    public void deleteById(PatientVisitId patientVisitId) {
        patientVisitJpaRepository.deleteById(Long.valueOf(patientVisitId.getValue()));
    }

    @Override
    public long count() {
        return patientVisitJpaRepository.count();
    }

    @Override
    public long countByPatient(PatientCedula patientCedula) {
        return patientVisitJpaRepository.countByPatientCedula(patientCedula.getValue());
    }

    @Override
    public long countCompleted() {
        return patientVisitJpaRepository.findAll()
                .stream()
                .mapToLong(entity -> entity.isCompleted() ? 1 : 0)
                .sum();
    }

    @Override
    public List<PatientVisit> findByVisitDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return patientVisitJpaRepository.findByVisitDateTimeBetween(startDateTime, endDateTime)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // Métodos de conversión entre dominio y entidad

    private PatientVisitEntity toEntity(PatientVisit patientVisit) {
        // Basic implementation - would need PatientVisit domain model details
        PatientVisitEntity entity = new PatientVisitEntity();
        // Set basic fields based on PatientVisit model structure
        // This is a placeholder implementation
        return entity;
    }

    private PatientVisit toDomain(PatientVisitEntity entity) {
        // Basic implementation - would need PatientVisit domain model details
        // This is a placeholder implementation that needs the actual PatientVisit model
        return null; // Placeholder - needs proper PatientVisit domain model
    }
}