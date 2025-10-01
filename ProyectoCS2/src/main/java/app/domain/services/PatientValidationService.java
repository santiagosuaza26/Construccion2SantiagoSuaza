package app.domain.services;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import app.domain.exception.PatientValidationException;
import app.domain.model.Patient;
import app.domain.model.Role;
import app.domain.port.PatientRepository;
import app.domain.services.AuthenticationService.AuthenticatedUser;

/**
 * Specialized service for patient validations
 * Follows SRP principle - single responsibility: validate patient business rules
 */
public class PatientValidationService {
    private final PatientRepository patientRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final List<String> ALLOWED_GENDERS = List.of("masculino", "femenino", "otro");

    public PatientValidationService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Valida que un paciente cumpla con todas las reglas de negocio
     */
    public void validatePatientForRegistration(Patient patient) {
        validateBasicPatientData(patient);
        validateAgeConstraints(patient.getBirthDate());
        validateGender(patient.getGender());
        validatePhoneFormat(patient.getPhone());
        validateEmailFormat(patient.getEmail());
        validateAddressLength(patient.getAddress());
    }

    /**
     * Valida que un paciente cumpla con todas las reglas de negocio para actualización
     */
    public void validatePatientForUpdate(Patient existingPatient, Patient updatedPatient) {
        validatePatientForRegistration(updatedPatient);

        // Validaciones adicionales para actualización
        validateUpdatePermissions(existingPatient, updatedPatient);
    }

    /**
     * Valida datos básicos del paciente
     */
    private void validateBasicPatientData(Patient patient) {
        if (patient == null) {
            throw new PatientValidationException("Patient cannot be null");
        }

        if (patient.getIdCard() == null || patient.getIdCard().trim().isEmpty()) {
            throw new PatientValidationException("Patient ID card is required");
        }

        if (patient.getFullName() == null || patient.getFullName().trim().isEmpty()) {
            throw new PatientValidationException("Patient full name is required");
        }

        if (patient.getBirthDate() == null) {
            throw new PatientValidationException("Patient birth date is required");
        }

        if (patient.getCredentials() == null) {
            throw new PatientValidationException("Patient credentials are required");
        }

        if (patient.getEmergencyContact() == null) {
            throw new PatientValidationException("Patient emergency contact is required");
        }
    }

    /**
     * Valida restricciones de edad
     */
    private void validateAgeConstraints(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now())) {
            throw new PatientValidationException("Birth date cannot be in the future");
        }

        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age > 150) {
            throw new PatientValidationException("Patient age cannot exceed 150 years");
        }

        if (age < 0) {
            throw new PatientValidationException("Invalid birth date");
        }
    }

    /**
     * Valida género permitido
     */
    private void validateGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            throw new PatientValidationException("Patient gender is required");
        }

        String normalizedGender = gender.toLowerCase().trim();
        if (!ALLOWED_GENDERS.contains(normalizedGender)) {
            throw new PatientValidationException("Gender must be: masculino, femenino, or otro");
        }
    }

    /**
     * Valida formato de teléfono (exactamente 10 dígitos)
     */
    private void validatePhoneFormat(String phone) {
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new PatientValidationException("Patient phone must have exactly 10 digits");
        }
    }

    /**
     * Valida formato de email
     */
    private void validateEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new PatientValidationException("Patient email is required");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new PatientValidationException("Invalid email format");
        }
    }

    /**
     * Valida longitud de dirección
     */
    private void validateAddressLength(String address) {
        if (address != null && address.length() > 30) {
            throw new PatientValidationException("Patient address must be maximum 30 characters");
        }
    }

    /**
     * Valida permisos para actualización
     */
    private void validateUpdatePermissions(Patient existingPatient, Patient updatedPatient) {
        // No permitir cambios en datos críticos sin autorización especial
        if (!existingPatient.getIdCard().equals(updatedPatient.getIdCard())) {
            throw new PatientValidationException("Cannot change patient ID card during update");
        }
    }

    /**
     * Valida unicidad de datos para nuevo paciente
     */
    public void validatePatientUniqueness(String idCard, String username) {
        if (patientRepository.existsByIdCard(idCard)) {
            throw new PatientValidationException("Patient with ID card " + idCard + " already exists");
        }

        if (patientRepository.existsByUsername(username)) {
            throw new PatientValidationException("Username " + username + " already exists");
        }
    }

    /**
     * Valida unicidad de datos para actualización de paciente
     */
    public void validatePatientUniquenessForUpdate(String currentIdCard, String newIdCard, String username) {
        if (newIdCard != null && !newIdCard.equals(currentIdCard)) {
            if (patientRepository.existsByIdCard(newIdCard)) {
                throw new PatientValidationException("Patient with ID card " + newIdCard + " already exists");
            }
        }

        // Buscar si hay otro paciente con el mismo username
        if (username != null) {
            // TODO: Implementar método para buscar por username excluyendo el paciente actual
        }
    }

    /**
     * Valida que el paciente pueda ser eliminado
     */
    public void validatePatientCanBeRemoved(Patient patient, AuthenticatedUser currentUser) {
        // Validar que el usuario tenga permisos para eliminar
        if (!currentUser.canRegisterPatients()) {
            throw new PatientValidationException.UnauthorizedPatientAccessException(currentUser.getIdCard(), "remove patients");
        }

        // Validar que el paciente no tenga dependencias críticas
        // TODO: Implementar validaciones de dependencias (facturas pendientes, órdenes activas, etc.)
    }

    /**
     * Valida acceso a información de paciente
     */
    public void validatePatientAccess(Patient patient, AuthenticatedUser currentUser) {
        if (!currentUser.canAccessPatientData()) {
            throw new PatientValidationException.UnauthorizedPatientAccessException(currentUser.getIdCard(), "access patient data");
        }

        // Si es paciente, solo puede acceder a su propia información
        if (currentUser.getRole() == Role.PATIENT &&
            !currentUser.getIdCard().equals(patient.getIdCard())) {
            throw new PatientValidationException.UnauthorizedPatientAccessException(currentUser.getIdCard(), "access other patient information");
        }
    }
}