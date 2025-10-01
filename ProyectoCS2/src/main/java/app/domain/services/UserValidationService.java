package app.domain.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import app.domain.exception.DomainValidationException;
import app.domain.model.Role;
import app.domain.model.User;
import app.domain.port.UserRepository;

/**
 * Servicio especializado para validaciones de usuarios del sistema
 * Sigue principio SRP - responsabilidad única: validar usuarios del sistema
 */
public class UserValidationService {
    private final UserRepository userRepository;
    private static final List<String> ALLOWED_ROLES = List.of("ADMINISTRATIVE", "HUMAN_RESOURCES", "DOCTOR", "NURSE");

    public UserValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Valida que un usuario cumpla con todas las reglas de negocio
     */
    public void validateUserForRegistration(User user) {
        validateBasicUserData(user);
        validateAgeConstraints(user.getBirthDate());
        validateEmailFormat(user.getEmail());
        validatePhoneFormat(user.getPhone());
        validateAddressLength(user.getAddress());
        validateRole(user.getRole());
    }

    /**
     * Valida que un usuario cumpla con todas las reglas de negocio para actualización
     */
    public void validateUserForUpdate(User existingUser, User updatedUser) {
        validateUserForRegistration(updatedUser);

        // Validaciones adicionales para actualización
        validateUpdatePermissions(existingUser, updatedUser);
    }

    /**
     * Valida datos básicos del usuario
     */
    private void validateBasicUserData(User user) {
        if (user == null) {
            throw new DomainValidationException("User cannot be null");
        }

        if (user.getIdCard() == null || user.getIdCard().trim().isEmpty()) {
            throw new DomainValidationException("User ID card is required");
        }

        if (!user.getIdCard().matches("\\d+")) {
            throw new DomainValidationException("User ID card must contain only numbers");
        }

        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new DomainValidationException("User full name is required");
        }

        if (user.getBirthDate() == null) {
            throw new DomainValidationException("User birth date is required");
        }

        if (user.getCredentials() == null) {
            throw new DomainValidationException("User credentials are required");
        }
    }

    /**
     * Valida restricciones de edad
     */
    private void validateAgeConstraints(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now())) {
            throw new DomainValidationException("Birth date cannot be in the future");
        }

        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age > 150) {
            throw new DomainValidationException("User age cannot exceed 150 years");
        }

        if (age < 18) {
            throw new DomainValidationException("User must be at least 18 years old");
        }
    }

    /**
     * Valida formato de email
     */
    private void validateEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new DomainValidationException("User email is required");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new DomainValidationException("Invalid email format");
        }
    }

    /**
     * Valida formato de teléfono
     */
    private void validatePhoneFormat(String phone) {
        if (phone == null || !phone.matches("\\d{1,10}")) {
            throw new DomainValidationException("User phone must contain between 1 and 10 digits");
        }
    }

    /**
     * Valida longitud de dirección
     */
    private void validateAddressLength(String address) {
        if (address != null && address.length() > 30) {
            throw new DomainValidationException("User address must be maximum 30 characters");
        }
    }

    /**
     * Valida rol permitido
     */
    private void validateRole(Role role) {
        if (role == null) {
            throw new DomainValidationException("User role is required");
        }

        if (role == Role.PATIENT) {
            throw new DomainValidationException("Patients should use Patient entity, not User entity");
        }
    }

    /**
     * Valida permisos para actualización
     */
    private void validateUpdatePermissions(User existingUser, User updatedUser) {
        // No permitir cambios en datos críticos sin autorización especial
        if (!existingUser.getIdCard().equals(updatedUser.getIdCard())) {
            throw new DomainValidationException("Cannot change user ID card during update");
        }
    }

    /**
     * Valida unicidad de datos para nuevo usuario
     */
    public void validateUserUniqueness(String idCard, String username) {
        if (userRepository.existsByIdCard(idCard)) {
            throw new DomainValidationException("User with ID card " + idCard + " already exists");
        }

        if (userRepository.existsByUsername(username)) {
            throw new DomainValidationException("Username " + username + " already exists");
        }
    }

    /**
     * Valida unicidad de datos para actualización de usuario
     */
    public void validateUserUniquenessForUpdate(String currentIdCard, String newIdCard, String username) {
        if (newIdCard != null && !newIdCard.equals(currentIdCard)) {
            if (userRepository.existsByIdCard(newIdCard)) {
                throw new DomainValidationException("User with ID card " + newIdCard + " already exists");
            }
        }

        // Buscar si hay otro usuario con el mismo username
        if (username != null) {
            // TODO: Implementar método para buscar por username excluyendo el usuario actual
        }
    }
}