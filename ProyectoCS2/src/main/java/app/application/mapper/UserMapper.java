package app.application.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.application.dto.request.CreateUserRequest;
import app.application.dto.response.UserResponse;
import app.domain.model.Credentials;
import app.domain.model.Role;
import app.domain.model.User;

/**
 * UserMapper - Mapper completo para conversiones entre DTOs y Domain Models
 * 
 * RESPONSABILIDADES:
 * - Convertir CreateUserRequest → User (Domain)
 * - Convertir User (Domain) → UserResponse
 * - Validar y transformar formatos de datos
 * - Manejar errores de conversión
 * - Aplicar reglas de negocio durante la conversión
 * 
 * REGLAS IMPLEMENTADAS:
 * - Fechas en formato DD/MM/YYYY
 * - Validación de roles permitidos
 * - Creación segura de credenciales
 * - Manejo de campos opcionales
 */
@Component
public class UserMapper {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /**
     * Convierte CreateUserRequest → User (Domain Model)
     * 
     * TRANSFORMACIONES:
     * - String birthDate (DD/MM/YYYY) → LocalDate
     * - String role → Role enum
     * - username + password → Credentials object
     * - Validaciones de integridad de datos
     * 
     * @param request DTO con datos del usuario a crear
     * @return User domain model listo para persistir
     * @throws IllegalArgumentException si hay datos inválidos
     */
    public User toUser(CreateUserRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("CreateUserRequest cannot be null");
        }
        
        try {
            // 1. Convertir fecha de nacimiento
            LocalDate birthDate = parseDate(request.getBirthDate());
            
            // 2. Convertir rol
            Role role = parseRole(request.getRole());
            
            // 3. Crear credenciales de forma segura
            Credentials credentials = createCredentials(request.getUsername(), request.getPassword());
            
            // 4. Procesar dirección opcional
            String processedAddress = processAddress(request.getAddress());
            
            // 5. Crear el usuario con todas las validaciones
            User user = new User(
                request.getFullName().trim(),
                request.getIdCard().trim(),
                request.getEmail().toLowerCase().trim(), // Normalizar email
                request.getPhone().trim(),
                birthDate,
                processedAddress,
                role,
                credentials
            );
            
            return user;
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting CreateUserRequest to User: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte User (Domain) → UserResponse (DTO)
     * 
     * TRANSFORMACIONES:
     * - LocalDate → String (DD/MM/YYYY)
     * - Role enum → String
     * - Ocultar información sensible (passwords)
     * - Agregar información calculada (fechas, estados)
     * 
     * @param user Domain model a convertir
     * @return UserResponse DTO para enviar al cliente
     */
    public UserResponse toResponse(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        try {
            UserResponse response = new UserResponse();
            
            // Información básica
            response.setIdCard(user.getIdCard());
            response.setFullName(user.getFullName());
            response.setEmail(user.getEmail());
            response.setPhone(user.getPhone());
            response.setRole(user.getRole().name());
            
            // Convertir fecha de nacimiento
            if (user.getBirthDate() != null) {
                response.setBirthDate(user.getBirthDate().format(DATE_FORMATTER));
            }
            
            // Procesar dirección opcional
            response.setAddress(user.getAddress());
            
            // Información de credenciales (solo username, NO password)
            if (user.getCredentials() != null) {
                response.setUsername(user.getCredentials().getUsername());
            }
            
            // Agregar timestamp de creación (simulado - en implementación real vendría de auditoría)
            response.setCreatedDate(LocalDate.now().format(DATE_FORMATTER));
            
            return response;
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting User to UserResponse: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte lista de Users → lista de UserResponses
     * Útil para endpoints que retornan múltiples usuarios
     */
    public List<UserResponse> toResponseList(List<User> users) {
        if (users == null) {
            throw new IllegalArgumentException("User list cannot be null");
        }
        
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Crear UserResponse básico (solo información esencial)
     * Útil para listas o cuando no se necesita información completa
     */
    public UserResponse toBasicResponse(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        return new UserResponse(
            user.getIdCard(),
            user.getFullName(),
            user.getRole().name(),
            user.getEmail()
        );
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE UTILIDAD
    // =============================================================================
    
    /**
     * Parsear fecha desde string DD/MM/YYYY a LocalDate
     * Con manejo robusto de errores
     */
    private LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            throw new IllegalArgumentException("Birth date is required");
        }
        
        try {
            LocalDate date = LocalDate.parse(dateString, DATE_FORMATTER);
            
            // Validar que la fecha sea lógica
            if (date.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Birth date cannot be in the future");
            }
            
            if (date.isBefore(LocalDate.now().minusYears(150))) {
                throw new IllegalArgumentException("Birth date cannot be more than 150 years ago");
            }
            
            return date;
            
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use DD/MM/YYYY format", e);
        }
    }
    
    /**
     * Parsear rol desde string a Role enum
     * Con validación de roles permitidos
     */
    private Role parseRole(String roleString) {
        if (roleString == null || roleString.isBlank()) {
            throw new IllegalArgumentException("Role is required");
        }
        
        try {
            Role role = Role.valueOf(roleString.toUpperCase());
            
            // Validar que no sea PATIENT (los pacientes usan Patient entity)
            if (role == Role.PATIENT) {
                throw new IllegalArgumentException("PATIENT role should use Patient entity, not User entity");
            }
            
            return role;
            
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + roleString + 
                ". Allowed roles: HUMAN_RESOURCES, ADMINISTRATIVE, SUPPORT, NURSE, DOCTOR", e);
        }
    }
    
    /**
     * Crear credenciales de forma segura
     * Con validaciones adicionales
     */
    private Credentials createCredentials(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        try {
            // El constructor de Credentials ya hace las validaciones
            // pero agregamos contexto específico del mapper
            return new Credentials(username.trim(), password);
            
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid credentials: " + e.getMessage(), e);
        }
    }
    
    /**
     * Procesar dirección opcional
     * Normalizar y validar longitud
     */
    private String processAddress(String address) {
        if (address == null || address.isBlank()) {
            return null; // Dirección es opcional
        }
        
        String trimmedAddress = address.trim();
        
        // Ya está validado en el DTO, pero double-check por seguridad
        if (trimmedAddress.length() > 30) {
            throw new IllegalArgumentException("Address must be maximum 30 characters");
        }
        
        return trimmedAddress;
    }
    
    // =============================================================================
    // MÉTODOS DE UTILIDAD PARA VALIDACIONES ADICIONALES
    // =============================================================================
    
    /**
     * Validar que el request tenga todos los campos requeridos
     * (Complementa las validaciones de Bean Validation)
     */
    public void validateCreateUserRequest(CreateUserRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("CreateUserRequest cannot be null");
        }
        
        // Validaciones específicas del negocio que no están en Bean Validation
        if (request.getFullName() != null && request.getFullName().trim().length() < 2) {
            throw new IllegalArgumentException("Full name must be at least 2 characters");
        }
        
        // Validar que el email no sea de dominios bloqueados (ejemplo)
        if (request.getEmail() != null) {
            String email = request.getEmail().toLowerCase();
            if (email.contains("tempmail") || email.contains("10minutemail")) {
                throw new IllegalArgumentException("Temporary email addresses are not allowed");
            }
        }
        
        // Validar consistencia entre campos
        if (request.getRole() != null && request.getRole().equals("DOCTOR")) {
            if (request.getPhone() == null || request.getPhone().length() < 10) {
                throw new IllegalArgumentException("Doctors must provide a complete phone number");
            }
        }
    }
    
    /**
     * Crear un User con valores por defecto para testing
     * Útil para pruebas unitarias
     */
    public User createTestUser(String idCard, String name, String role) {
        CreateUserRequest testRequest = new CreateUserRequest(
            name,
            idCard,
            name.toLowerCase().replace(" ", ".") + "@clinica.com",
            "1234567890",
            "01/01/1990",
            null, // Sin dirección
            role,
            name.toLowerCase().replace(" ", "") + "123",
            "TestPass123!"
        );
        
        return toUser(testRequest);
    }
    
    /**
     * Método para debugging - convierte User a string legible
     */
    public String userToDebugString(User user) {
        if (user == null) return "User[null]";
        
        return String.format("User[id=%s, name=%s, role=%s, email=%s, phone=%s]",
            user.getIdCard(),
            user.getFullName(),
            user.getRole(),
            user.getEmail(),
            user.getPhone()
        );
    }
}