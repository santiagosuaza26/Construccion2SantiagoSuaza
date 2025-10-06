package app.clinic.application.service;

import org.springframework.stereotype.Service;

import app.clinic.application.dto.user.LoginRequestDTO;
import app.clinic.application.dto.user.LoginResponseDTO;
import app.clinic.application.dto.user.UserDTO;

/**
 * Service for handling user authentication operations.
 * Provides login functionality and credential validation.
 */
@Service
public class AuthenticationApplicationService {

    /**
     * Authenticates a user with the provided credentials.
     * For demo purposes, uses hardcoded credentials.
     *
     * @param loginRequest The login request containing username and password
     * @return LoginResponseDTO with authentication result
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            // Demo authentication - in production, this would query the database
            if ("admin".equals(loginRequest.getUsername()) && "Admin123!@#".equals(loginRequest.getPassword())) {
                UserDTO userDTO = createDemoUserDTO("admin");
                String token = generateToken(userDTO);
                return new LoginResponseDTO(true, token, userDTO);
            }

            if ("doctor".equals(loginRequest.getUsername()) && "Doctor123!@#".equals(loginRequest.getPassword())) {
                UserDTO userDTO = createDemoUserDTO("doctor");
                String token = generateToken(userDTO);
                return new LoginResponseDTO(true, token, userDTO);
            }

            // Try alternative passwords for flexibility
            if ("admin".equals(loginRequest.getUsername()) && "admin123".equals(loginRequest.getPassword())) {
                UserDTO userDTO = createDemoUserDTO("admin");
                String token = generateToken(userDTO);
                return new LoginResponseDTO(true, token, userDTO);
            }

            if ("doctor".equals(loginRequest.getUsername()) && "doctor123".equals(loginRequest.getPassword())) {
                UserDTO userDTO = createDemoUserDTO("doctor");
                String token = generateToken(userDTO);
                return new LoginResponseDTO(true, token, userDTO);
            }

            return new LoginResponseDTO(false, "Invalid credentials");

        } catch (Exception e) {
            return new LoginResponseDTO(false, "Internal server error");
        }
    }

    /**
     * Creates a demo UserDTO for testing purposes.
     */
    private UserDTO createDemoUserDTO(String username) {
        UserDTO dto = new UserDTO();
        if ("admin".equals(username)) {
            dto.setCedula("12345678");
            dto.setUsername("admin");
            dto.setFullName("Administrador Sistema");
            dto.setRole("HUMAN_RESOURCES");
            dto.setActive(true);
            dto.setAge(40);
        } else if ("doctor".equals(username)) {
            dto.setCedula("87654321");
            dto.setUsername("doctor");
            dto.setFullName("Carlos Médico");
            dto.setRole("DOCTOR");
            dto.setActive(true);
            dto.setAge(35);
        }
        return dto;
    }

    /**
     * Validates user password.
     * In a production environment, this should use proper password hashing (bcrypt, etc.)
     *
     * @param providedPassword The password provided by the user
     * @param storedPassword The password stored in the database
     * @return true if passwords match
     */
    private boolean validatePassword(String providedPassword, String storedPassword) {
        // For development purposes, we'll do a simple comparison
        // In production, use BCrypt or similar for password hashing
        return providedPassword != null && providedPassword.equals(storedPassword);
    }

    /**
     * Gets demo password for a user (for development purposes).
     * In production, passwords should be properly hashed and stored.
     *
     * @param username The username
     * @return The demo password for the user
     */
    private String getDemoPasswordForUser(String username) {
        // Demo passwords for development - in production, use proper password hashing
        switch (username.toLowerCase()) {
            case "admin": return "Admin123!@#";
            case "doctor": return "Doctor123!@#";
            case "nurse": return "nurse123";
            case "hr": return "hr123";
            case "staff": return "staff123";
            default: return "password123";
        }
    }

    /**
     * Generates an authentication token for the user.
     * In a production environment, this should generate a proper JWT token.
     *
     * @param user The authenticated user
     * @return The generated token
     */
    private String generateToken(UserDTO user) {
        // For development purposes, we'll generate a simple token
        // In production, use a proper JWT library like jjwt
        return "dev-token-" + user.getCedula() + "-" + System.currentTimeMillis();
    }
}