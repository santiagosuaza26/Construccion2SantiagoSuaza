package app.clinic.infrastructure.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.valueobject.Credentials;
import app.clinic.domain.repository.UserRepository;
import app.clinic.domain.service.UserValidationService;

@Service
public class UserValidationServiceImpl implements UserValidationService {

    private final UserRepository userRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{1,10}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    public UserValidationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validateCredentialsUniqueness(Credentials credentials) {
        // Validar que el username sea único
        if (userRepository.findByUsername(credentials.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + credentials.getUsername().getValue());
        }
    }

    @Override
    public void validateUserData(String fullName, String identificationNumber, String email, String phone,
                                String dateOfBirth, String address, String role, String username, String password) {
        // Validar nombre completo
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }

        // Validar cédula única
        if (identificationNumber == null || identificationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Identification number is required");
        }
        if (userRepository.existsByIdentificationNumber(new app.clinic.domain.model.valueobject.Id(identificationNumber))) {
            throw new IllegalArgumentException("Identification number already exists: " + identificationNumber);
        }

        // Validar email
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Validar teléfono
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone is required");
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("Phone must contain only digits (1-10 digits)");
        }

        // Validar fecha de nacimiento
        if (dateOfBirth == null || dateOfBirth.trim().isEmpty()) {
            throw new IllegalArgumentException("Date of birth is required");
        }
        try {
            LocalDate birthDate = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate minDate = LocalDate.now().minusYears(150);
            if (birthDate.isBefore(minDate)) {
                throw new IllegalArgumentException("Date of birth cannot be more than 150 years ago");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use DD/MM/YYYY");
        }

        // Validar dirección
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (address.length() > 30) {
            throw new IllegalArgumentException("Address cannot exceed 30 characters");
        }

        // Validar rol
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }
        String[] validRoles = {"Médico", "Enfermera", "Personal administrativo", "Recursos Humanos", "Soporte"};
        boolean validRole = false;
        for (String validRoleOption : validRoles) {
            if (validRoleOption.equals(role)) {
                validRole = true;
                break;
            }
        }
        if (!validRole) {
            throw new IllegalArgumentException("Invalid role. Must be one of: Médico, Enfermera, Personal administrativo, Recursos Humanos, Soporte");
        }

        // Validar username
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (username.length() > 15) {
            throw new IllegalArgumentException("Username cannot exceed 15 characters");
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException("Username must contain only letters and numbers");
        }
        if (userRepository.findByUsername(new app.clinic.domain.model.valueobject.Username(username)).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        // Validar contraseña
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException("Password must contain at least 8 characters, including uppercase, lowercase, number and special character");
        }
    }
}