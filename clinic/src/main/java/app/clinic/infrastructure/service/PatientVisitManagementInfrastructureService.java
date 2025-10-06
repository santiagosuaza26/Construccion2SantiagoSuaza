package app.clinic.infrastructure.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientVisit;
import app.clinic.domain.model.PatientVisitId;
import app.clinic.domain.port.PatientVisitRepository;

/**
 * Infrastructure service for patient visit management operations.
 * Provides business logic implementation for patient visit operations.
 */
@Service
@Transactional
public class PatientVisitManagementInfrastructureService {

    private final PatientVisitRepository patientVisitRepository;

    public PatientVisitManagementInfrastructureService(PatientVisitRepository patientVisitRepository) {
        this.patientVisitRepository = patientVisitRepository;
    }

    /**
     * Creates or updates a patient visit.
     */
    public PatientVisit savePatientVisit(PatientVisit patientVisit) {
        return patientVisitRepository.save(patientVisit);
    }

    /**
     * Finds a patient visit by ID.
     */
    @Transactional(readOnly = true)
    public Optional<PatientVisit> findPatientVisitById(PatientVisitId patientVisitId) {
        return patientVisitRepository.findById(patientVisitId);
    }

    /**
     * Finds all patient visits for a specific patient.
     */
    @Transactional(readOnly = true)
    public List<PatientVisit> findPatientVisitsByPatient(PatientCedula patientCedula) {
        return patientVisitRepository.findByPatientCedula(patientCedula);
    }

    /**
     * Finds all patient visits.
     */
    @Transactional(readOnly = true)
    public List<PatientVisit> findAllPatientVisits() {
        return patientVisitRepository.findAll();
    }

    /**
     * Finds all completed patient visits.
     */
    @Transactional(readOnly = true)
    public List<PatientVisit> findAllCompletedPatientVisits() {
        return patientVisitRepository.findAllCompleted();
    }

    /**
     * Checks if a patient visit exists with the given ID.
     */
    @Transactional(readOnly = true)
    public boolean patientVisitExistsById(PatientVisitId patientVisitId) {
        return patientVisitRepository.existsById(patientVisitId);
    }

    /**
     * Deletes a patient visit by ID.
     */
    public void deletePatientVisitById(PatientVisitId patientVisitId) {
        patientVisitRepository.deleteById(patientVisitId);
    }

    /**
     * Counts total number of patient visits.
     */
    @Transactional(readOnly = true)
    public long countPatientVisits() {
        return patientVisitRepository.count();
    }

    /**
     * Counts patient visits by patient.
     */
    @Transactional(readOnly = true)
    public long countPatientVisitsByPatient(PatientCedula patientCedula) {
        return patientVisitRepository.countByPatient(patientCedula);
    }

    /**
     * Counts completed patient visits.
     */
    @Transactional(readOnly = true)
    public long countCompletedPatientVisits() {
        return patientVisitRepository.countCompleted();
    }
}