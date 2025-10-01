package app.domain.services;

import app.domain.exception.DomainValidationException;
import app.domain.model.Credentials;

/**
 * Servicio especializado para validaciones de credenciales
 * Sigue principio SRP - responsabilidad única: validar credenciales de acceso
 */
public class CredentialsValidationService {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_USERNAME_LENGTH = 15;

    /**
     * Valida credenciales para autenticación
     */
    public void validateCredentialsForAuthentication(Credentials credentials) {
        if (credentials == null) {
            throw new DomainValidationException("Credentials cannot be null");
        }

        validateUsername(credentials.getUsername());
        validatePassword(credentials.getPassword());
    }

    /**
     * Valida credenciales para registro de nuevo usuario
     */
    public void validateCredentialsForRegistration(Credentials credentials) {
        validateCredentialsForAuthentication(credentials);

        // Validaciones adicionales para registro
        validateUsernameFormat(credentials.getUsername());
        validatePasswordStrength(credentials.getPassword());
    }

    /**
     * Valida formato de nombre de usuario
     */
    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new DomainValidationException("Username is required");
        }

        if (username.length() > MAX_USERNAME_LENGTH) {
            throw new DomainValidationException("Username cannot exceed " + MAX_USERNAME_LENGTH + " characters");
        }
    }

    /**
     * Valida formato específico de username (solo letras y números)
     */
    private void validateUsernameFormat(String username) {
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            throw new DomainValidationException("Username must contain only letters and numbers");
        }
    }

    /**
     * Valida contraseña básica
     */
    private void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new DomainValidationException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
        }
    }

    /**
     * Valida fortaleza de contraseña
     */
    private void validatePasswordStrength(String password) {
        if (!password.matches(".*[A-Z].*")) {
            throw new DomainValidationException("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            throw new DomainValidationException("Password must contain at least one number");
        }

        if (!password.matches(".*[^A-Za-z0-9].*")) {
            throw new DomainValidationException("Password must contain at least one special character");
        }
    }

    /**
     * Valida cambio de contraseña
     */
    public void validatePasswordChange(String currentPassword, String newPassword) {
        if (currentPassword == null || currentPassword.isEmpty()) {
            throw new DomainValidationException("Current password is required");
        }

        if (newPassword == null || newPassword.isEmpty()) {
            throw new DomainValidationException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
        }

        validatePasswordStrength(newPassword);

        if (currentPassword.equals(newPassword)) {
            throw new DomainValidationException("New password must be different from current password");
        }
    }

    /**
     * Valida que las contraseñas coincidan
     */
    public void validatePasswordConfirmation(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            throw new DomainValidationException("Both password and confirmation are required");
        }

        if (!password.equals(confirmPassword)) {
            throw new DomainValidationException("Password confirmation does not match");
        }
    }
}