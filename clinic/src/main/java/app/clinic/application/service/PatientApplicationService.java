package app.clinic.application.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import app.clinic.application.dto.patient.CreatePatientDTO;
import app.clinic.application.dto.patient.PatientDTO;
import app.clinic.application.dto.patient.UpdatePatientDTO;
import app.clinic.application.mapper.PatientMapper;
import app.clinic.domain.model.Patient;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientId;
import app.clinic.domain.model.PatientUsername;
import app.clinic.domain.service.PatientDomainService;

/**
 * Application service for patient management operations.
 * Coordinates between REST controllers and domain services.
 * Handles patient-related use cases and business operations.
 */
@Service
public class PatientApplicationService {

    private final PatientDomainService patientDomainService;

    public PatientApplicationService(PatientDomainService patientDomainService) {
        this.patientDomainService = patientDomainService;
    }

    /**
     * Registers a new patient based on the provided DTO.
     */
    public PatientDTO registerPatient(CreatePatientDTO createPatientDTO) {
        // Convert DTO to domain entity using mapper
        Patient patient = PatientMapper.toDomainEntity(createPatientDTO);

        // Create patient using domain service
        Patient createdPatient = patientDomainService.registerPatient(patient);

        // Convert back to DTO and return
        return PatientMapper.toDTO(createdPatient);
    }

    /**
     * Updates an existing patient based on the provided DTO.
     */
    public PatientDTO updatePatient(UpdatePatientDTO updatePatientDTO) {
        // Find existing patient by cedula
        Optional<Patient> existingPatient = patientDomainService.findPatientByCedula(PatientCedula.of(updatePatientDTO.getCedula()));

        if (existingPatient.isPresent()) {
            // Update the existing patient with new data
            Patient updatedPatient = PatientMapper.updateEntity(existingPatient.get(), updatePatientDTO);
            Patient savedPatient = patientDomainService.updatePatient(updatedPatient);
            return PatientMapper.toDTO(savedPatient);
        } else {
            throw new IllegalArgumentException("Patient not found with cedula: " + updatePatientDTO.getCedula());
        }
    }

    /**
     * Finds a patient by their cedula.
     */
    public Optional<PatientDTO> findPatientByCedula(String cedula) {
        Optional<Patient> patient = patientDomainService.findPatientByCedula(PatientCedula.of(cedula));
        return patient.map(PatientMapper::toDTO);
    }

    /**
     * Finds a patient by their username.
     */
    public Optional<PatientDTO> findPatientByUsername(String username) {
        Optional<Patient> patient = patientDomainService.findPatientByUsername(PatientUsername.of(username));
        return patient.map(PatientMapper::toDTO);
    }

    /**
     * Finds a patient by their ID.
     */
    public Optional<PatientDTO> findPatientById(String patientId) {
        Optional<Patient> patient = patientDomainService.findPatientById(PatientId.of(patientId));
        return patient.map(PatientMapper::toDTO);
    }

    /**
     * Finds all patients.
     */
    public List<PatientDTO> findAllPatients() {
        List<Patient> patients = patientDomainService.findAllPatients();
        return patients.stream()
                     .map(PatientMapper::toDTO)
                     .collect(Collectors.toList());
    }

    /**
     * Deletes a patient by their cedula.
     */
    public void deletePatientByCedula(String cedula) {
        patientDomainService.deletePatientByCedula(PatientCedula.of(cedula));
    }

    /**
     * Deletes a patient by their ID.
     */
    public void deletePatientById(String patientId) {
        patientDomainService.deletePatientById(PatientId.of(patientId));
    }

    /**
     * Checks if a patient has an active insurance policy.
     */
    public boolean hasActiveInsurance(String cedula) {
        Optional<Patient> patient = patientDomainService.findPatientByCedula(PatientCedula.of(cedula));
        return patient.map(p -> p.getInsurancePolicy() != null && p.getInsurancePolicy().isActive())
                     .orElse(false);
    }

    /**
     * Gets the age of a patient.
     */
    public int getPatientAge(String cedula) {
        Optional<Patient> patient = patientDomainService.findPatientByCedula(PatientCedula.of(cedula));
        return patient.map(p -> p.getAge().getValue()).orElse(0);
    }


}