package app.domain.services;

import app.domain.exception.DomainValidationException;
import app.domain.model.EmergencyContact;

/**
 * Servicio especializado para validaciones de contactos de emergencia
 * Sigue principio SRP - responsabilidad única: validar contactos de emergencia
 */
public class EmergencyContactValidationService {

    /**
     * Valida contacto de emergencia completo
     */
    public void validateEmergencyContact(EmergencyContact emergencyContact) {
        if (emergencyContact == null) {
            throw new DomainValidationException("Emergency contact cannot be null");
        }

        validateFirstName(emergencyContact.getFirstName());
        validateLastName(emergencyContact.getLastName());
        validateRelationship(emergencyContact.getRelationship());
        validatePhone(emergencyContact.getPhone());
    }

    /**
     * Valida nombre del contacto
     */
    private void validateFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new DomainValidationException("Emergency contact first name is required");
        }

        if (firstName.length() > 50) {
            throw new DomainValidationException("Emergency contact first name cannot exceed 50 characters");
        }
    }

    /**
     * Valida apellido del contacto
     */
    private void validateLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new DomainValidationException("Emergency contact last name is required");
        }

        if (lastName.length() > 50) {
            throw new DomainValidationException("Emergency contact last name cannot exceed 50 characters");
        }
    }

    /**
     * Valida relación con el paciente
     */
    private void validateRelationship(String relationship) {
        if (relationship == null || relationship.trim().isEmpty()) {
            throw new DomainValidationException("Emergency contact relationship is required");
        }

        if (relationship.length() > 30) {
            throw new DomainValidationException("Emergency contact relationship cannot exceed 30 characters");
        }

        // Validar relaciones comunes
        String normalizedRelationship = relationship.toLowerCase().trim();
        if (!normalizedRelationship.matches("^(padre|madre|hermano|hermana|hijo|hija|esposo|esposa|pareja|familiar|amigo|otro)$")) {
            throw new DomainValidationException("Emergency contact relationship must be: padre, madre, hermano, hermana, hijo, hija, esposo, esposa, pareja, familiar, amigo, or otro");
        }
    }

    /**
     * Valida teléfono del contacto de emergencia
     */
    private void validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new DomainValidationException("Emergency contact phone is required");
        }

        if (!phone.matches("\\d{10}")) {
            throw new DomainValidationException("Emergency contact phone must have exactly 10 digits");
        }

        // Validar que no sea un número inválido como 0000000000
        if (phone.equals("0000000000")) {
            throw new DomainValidationException("Emergency contact phone cannot be all zeros");
        }
    }

    /**
     * Valida que el contacto de emergencia no sea el mismo paciente
     */
    public void validateNotSameAsPatient(String emergencyPhone, String patientPhone) {
        if (emergencyPhone != null && emergencyPhone.equals(patientPhone)) {
            throw new DomainValidationException("Emergency contact phone cannot be the same as patient phone");
        }
    }

    /**
     * Valida que el contacto tenga información mínima requerida
     */
    public void validateMinimumRequiredInfo(EmergencyContact emergencyContact) {
        if (emergencyContact == null) {
            throw new DomainValidationException("Emergency contact is required");
        }

        boolean hasName = emergencyContact.getFirstName() != null && !emergencyContact.getFirstName().trim().isEmpty();
        boolean hasPhone = emergencyContact.getPhone() != null && !emergencyContact.getPhone().trim().isEmpty();

        if (!hasName || !hasPhone) {
            throw new DomainValidationException("Emergency contact must have at least first name and phone");
        }
    }
}