package app.clinic.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.Patient;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientId;
import app.clinic.domain.model.PatientUsername;
import app.clinic.domain.port.PatientRepository;

/**
 * Domain service for patient management operations.
 * Contains business logic for patient operations following domain-driven design principles.
 */
@Service
public class PatientDomainService {

    private final PatientRepository patientRepository;

    public PatientDomainService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Registers a new patient with validation.
     */
    public Patient registerPatient(Patient patient) {
        validatePatientForRegistration(patient);
        return patientRepository.save(patient);
    }

    /**
     * Updates an existing patient with validation.
     */
    public Patient updatePatient(Patient patient) {
        validatePatientForUpdate(patient);
        return patientRepository.save(patient);
    }

    /**
     * Finds a patient by ID.
     */
    public Optional<Patient> findPatientById(PatientId patientId) {
        return patientRepository.findById(patientId);
    }

    /**
     * Finds a patient by cedula.
     */
    public Optional<Patient> findPatientByCedula(PatientCedula cedula) {
        return patientRepository.findByCedula(cedula);
    }

    /**
     * Finds a patient by username.
     */
    public Optional<Patient> findPatientByUsername(PatientUsername username) {
        return patientRepository.findByUsername(username);
    }

    /**
     * Finds all patients.
     */
    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Deletes a patient by ID.
     */
    public void deletePatientById(PatientId patientId) {
        validatePatientCanBeDeleted(patientId);
        patientRepository.deleteById(patientId);
    }

    /**
     * Deletes a patient by cedula.
     */
    public void deletePatientByCedula(PatientCedula cedula) {
        validatePatientCanBeDeletedByCedula(cedula);
        patientRepository.deleteByCedula(cedula);
    }

    /**
     * Validates patient data for registration.
     */
    private void validatePatientForRegistration(Patient patient) {
        if (patientRepository.existsByCedula(patient.getCedula())) {
            throw new IllegalArgumentException("Patient with this cedula already exists");
        }
        if (patientRepository.existsByUsername(patient.getUsername())) {
            throw new IllegalArgumentException("Patient with this username already exists");
        }
        validatePatientEmergencyContacts(patient);
        validatePatientInsurancePolicy(patient);
    }

    /**
     * Validates patient data for update.
     */
    private void validatePatientForUpdate(Patient patient) {
        Optional<Patient> existingPatient = patientRepository.findById(extractPatientIdFromPatient(patient));
        if (existingPatient.isEmpty()) {
            throw new IllegalArgumentException("Patient to update does not exist");
        }
        validatePatientEmergencyContacts(patient);
        validatePatientInsurancePolicy(patient);
    }

    /**
     * Validates that the patient can be deleted.
     */
    private void validatePatientCanBeDeleted(PatientId patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isEmpty()) {
            throw new IllegalArgumentException("Patient to delete does not exist");
        }
        // Add additional business rules for patient deletion if needed
        // For example, check if patient has pending appointments or orders
    }

    /**
     * Validates that the patient can be deleted by cedula.
     */
    private void validatePatientCanBeDeletedByCedula(PatientCedula cedula) {
        Optional<Patient> patient = patientRepository.findByCedula(cedula);
        if (patient.isEmpty()) {
            throw new IllegalArgumentException("Patient to delete does not exist");
        }
        // Add additional business rules for patient deletion if needed
    }

    /**
     * Validates patient emergency contacts.
     */
    private void validatePatientEmergencyContacts(Patient patient) {
        List<app.clinic.domain.model.EmergencyContact> emergencyContacts = patient.getEmergencyContacts();
        if (emergencyContacts.size() > 1) {
            throw new IllegalArgumentException("Patient can have maximum one emergency contact");
        }
        // Add additional validation for emergency contact data if needed
    }

    /**
     * Validates patient insurance policy.
     */
    private void validatePatientInsurancePolicy(Patient patient) {
        app.clinic.domain.model.InsurancePolicy insurancePolicy = patient.getInsurancePolicy();
        if (insurancePolicy != null) {
            // Validate insurance policy data
            if (insurancePolicy.getExpirationDate().isExpired()) {
                throw new IllegalArgumentException("Insurance policy is expired");
            }
        }
    }

    /**
     * Extracts patient ID from patient object (placeholder for actual implementation).
     */
    private PatientId extractPatientIdFromPatient(Patient patient) {
        // Extract PatientId from the patient's cedula value
        return PatientId.of(patient.getCedula().getValue());
    }
}