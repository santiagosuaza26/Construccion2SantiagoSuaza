package app.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import app.application.dto.request.LoginRequest;
import app.application.dto.response.AuthResponse;
import app.application.dto.response.CommonResponse;
import app.domain.services.AuthenticationService;
import app.domain.services.AuthenticationService.AuthenticatedUser;

/**
 * AuthApplicationService - Servicio de aplicación para autenticación
 * 
 * CASOS DE USO IMPLEMENTADOS:
 * - Login de usuarios del sistema (HR, Admin, Support, Nurse, Doctor)
 * - Login de pacientes
 * - Logout y gestión de sesiones
 * - Verificación de permisos por rol
 * - Validación de acceso a recursos
 * 
 * REGLAS DE NEGOCIO:
 * - Usuarios del sistema y pacientes usan el mismo endpoint de login
 * - Permisos específicos por rol según documento
 * - Recursos Humanos NO puede ver pacientes, medicamentos, procedimientos
 * - Cada rol tiene acceso limitado según especificación
 * 
 * SEGURIDAD:
 * - Validación de credenciales
 * - Control de acceso granular
 * - Manejo seguro de información sensible
 */
@Service
public class AuthApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthApplicationService.class);

    private final AuthenticationService authenticationService;
    
    public AuthApplicationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    
    /**
     * CASO DE USO: Login
     * 
     * Autentica usuarios del sistema y pacientes usando las mismas credenciales.
     * Retorna información de la sesión y permisos específicos por rol.
     * 
     * @param request Credenciales de login (username, password)
     * @return Response con información del usuario autenticado y sus permisos
     */
    public CommonResponse<AuthResponse> login(LoginRequest request) {
        try {
            // 1. Validar request
            validateLoginRequest(request);
            
            // 2. Autenticar usando el servicio de dominio
            AuthenticatedUser authenticatedUser = authenticationService.authenticate(
                request.getUsername().trim(), 
                request.getPassword()
            );
            
            // 3. Crear respuesta con información del usuario y permisos
            AuthResponse authResponse = createAuthResponse(authenticatedUser);
            
            // 4. Log del login exitoso (en implementación real)
            logSuccessfulLogin(authenticatedUser);
            
            return CommonResponse.success("Login successful", authResponse);
            
        } catch (SecurityException e) {
            // Credenciales inválidas
            logFailedLogin(request.getUsername(), "Invalid credentials");
            return CommonResponse.error("Invalid username or password", "AUTH_001");
            
        } catch (IllegalArgumentException e) {
            // Error en validación del request
            return CommonResponse.error(e.getMessage(), "AUTH_002");
            
        } catch (Exception e) {
            // Error interno del sistema
            logSystemError("login", e);
            return CommonResponse.error("Internal system error during login", "AUTH_003");
        }
    }
    
    /**
     * CASO DE USO: Logout
     * 
     * Cierra la sesión del usuario y limpia la información de autenticación.
     * 
     * @param userId ID del usuario que está cerrando sesión
     * @return Response confirmando el logout
     */
    public CommonResponse<String> logout(String userId) {
        try {
            // Validar parámetros
            if (userId == null || userId.isBlank()) {
                return CommonResponse.error("User ID is required for logout", "AUTH_004");
            }
            
            // En implementación real: invalidar tokens, limpiar cache, etc.
            invalidateUserSession(userId);
            
            logSuccessfulLogout(userId);
            
            return CommonResponse.success("Logout successful");
            
        } catch (Exception e) {
            logSystemError("logout", e);
            return CommonResponse.error("Error during logout", "AUTH_005");
        }
    }
    
    /**
     * CASO DE USO: Verificar Permisos
     * 
     * Valida si un usuario autenticado tiene permiso para realizar una acción específica.
     * 
     * @param userId ID del usuario
     * @param requiredPermission Permiso requerido
     * @return Response indicando si tiene el permiso
     */
    public CommonResponse<Boolean> hasPermission(String userId, String requiredPermission) {
        try {
            if (userId == null || userId.isBlank()) {
                return CommonResponse.error("User ID is required", "AUTH_006");
            }
            
            if (requiredPermission == null || requiredPermission.isBlank()) {
                return CommonResponse.error("Permission is required", "AUTH_007");
            }
            
            // En implementación real: obtener usuario de cache/sesión
            Optional<AuthenticatedUser> userOpt = getCurrentUser(userId);
            
            if (userOpt.isEmpty()) {
                return CommonResponse.error("User session not found", "AUTH_008");
            }
            
            AuthenticatedUser user = userOpt.get();
            boolean hasPermission = checkSpecificPermission(user, requiredPermission);
            
            return CommonResponse.success("Permission checked", hasPermission);
            
        } catch (Exception e) {
            logSystemError("hasPermission", e);
            return CommonResponse.error("Error checking permissions", "AUTH_009");
        }
    }
    
    /**
     * CASO DE USO: Obtener Información del Usuario Actual
     * 
     * Retorna la información del usuario autenticado actualmente.
     * 
     * @param userId ID del usuario
     * @return Response con información del usuario
     */
    public CommonResponse<AuthResponse> getCurrentUserInfo(String userId) {
        try {
            if (userId == null || userId.isBlank()) {
                return CommonResponse.error("User ID is required", "AUTH_010");
            }
            
            Optional<AuthenticatedUser> userOpt = getCurrentUser(userId);
            
            if (userOpt.isEmpty()) {
                return CommonResponse.error("User session not found or expired", "AUTH_011");
            }
            
            AuthResponse authResponse = createAuthResponse(userOpt.get());
            
            return CommonResponse.success("User information retrieved", authResponse);
            
        } catch (Exception e) {
            logSystemError("getCurrentUserInfo", e);
            return CommonResponse.error("Error retrieving user information", "AUTH_012");
        }
    }
    
    /**
     * CASO DE USO: Validar Acceso a Recurso
     * 
     * Verifica si un usuario puede acceder a un recurso específico basado en su rol.
     * 
     * @param userId ID del usuario
     * @param resourceType Tipo de recurso (PATIENT, MEDICATION, PROCEDURE, etc.)
     * @param action Acción a realizar (READ, WRITE, DELETE)
     * @return Response indicando si puede acceder al recurso
     */
    public CommonResponse<Boolean> canAccessResource(String userId, String resourceType, String action) {
        try {
            // Validar parámetros
            if (userId == null || userId.isBlank()) {
                return CommonResponse.error("User ID is required", "AUTH_013");
            }
            
            if (resourceType == null || resourceType.isBlank()) {
                return CommonResponse.error("Resource type is required", "AUTH_014");
            }
            
            if (action == null || action.isBlank()) {
                return CommonResponse.error("Action is required", "AUTH_015");
            }
            
            // Obtener usuario actual
            Optional<AuthenticatedUser> userOpt = getCurrentUser(userId);
            
            if (userOpt.isEmpty()) {
                return CommonResponse.error("User session not found", "AUTH_016");
            }
            
            AuthenticatedUser user = userOpt.get();
            boolean canAccess = checkResourceAccess(user, resourceType, action);
            
            return CommonResponse.success("Resource access checked", canAccess);
            
        } catch (Exception e) {
            logSystemError("canAccessResource", e);
            return CommonResponse.error("Error checking resource access", "AUTH_017");
        }
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE UTILIDAD
    // =============================================================================
    
    /**
     * Validar LoginRequest con reglas específicas
     */
    private void validateLoginRequest(LoginRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Login request cannot be null");
        }
        
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        // Validación adicional: longitud mínima, caracteres permitidos, etc.
        if (request.getUsername().length() > 15) {
            throw new IllegalArgumentException("Username must be maximum 15 characters");
        }
        
        if (request.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }
    
    /**
     * Crear AuthResponse con información completa del usuario y permisos
     */
    private AuthResponse createAuthResponse(AuthenticatedUser user) {
        // Crear lista de permisos específicos según rol
        List<String> permissions = generatePermissionsList(user);
        
        // Generar token de sesión (simulado)
        String sessionToken = generateSessionToken(user);
        
        return new AuthResponse(
            user.getIdCard(),
            user.getFullName(),
            user.getRole().name(),
            user.isStaff(),
            permissions,
            sessionToken
        );
    }
    
    /**
     * Generar lista de permisos basada en el rol del usuario
     * Implementa las restricciones específicas del documento
     */
    private List<String> generatePermissionsList(AuthenticatedUser user) {
        List<String> permissions = new ArrayList<>();
        
        switch (user.getRole()) {
            case HUMAN_RESOURCES:
                // Solo gestión de usuarios, NO acceso a pacientes/medicamentos/procedimientos
                permissions.add("USER_CREATE");
                permissions.add("USER_READ");
                permissions.add("USER_UPDATE");
                permissions.add("USER_DELETE");
                // Explícitamente NO incluir: PATIENT_*, MEDICATION_*, PROCEDURE_*
                break;
                
            case ADMINISTRATIVE:
                // Gestión de pacientes y facturación
                permissions.add("PATIENT_CREATE");
                permissions.add("PATIENT_READ");
                permissions.add("PATIENT_UPDATE");
                permissions.add("APPOINTMENT_CREATE");
                permissions.add("APPOINTMENT_READ");
                permissions.add("INVOICE_CREATE");
                permissions.add("INVOICE_READ");
                permissions.add("INVOICE_PRINT");
                break;
                
            case SUPPORT:
                // Gestión completa de inventarios
                permissions.add("MEDICATION_CREATE");
                permissions.add("MEDICATION_READ");
                permissions.add("MEDICATION_UPDATE");
                permissions.add("MEDICATION_DELETE");
                permissions.add("PROCEDURE_CREATE");
                permissions.add("PROCEDURE_READ");
                permissions.add("PROCEDURE_UPDATE");
                permissions.add("PROCEDURE_DELETE");
                permissions.add("DIAGNOSTIC_CREATE");
                permissions.add("DIAGNOSTIC_READ");
                permissions.add("DIAGNOSTIC_UPDATE");
                permissions.add("DIAGNOSTIC_DELETE");
                permissions.add("SPECIALTY_CREATE");
                permissions.add("SPECIALTY_READ");
                permissions.add("SPECIALTY_UPDATE");
                permissions.add("SPECIALTY_DELETE");
                permissions.add("INVENTORY_MANAGE");
                break;
                
            case NURSE:
                // Atención al paciente y registro de signos vitales
                permissions.add("PATIENT_READ");
                permissions.add("VITAL_SIGNS_CREATE");
                permissions.add("VITAL_SIGNS_READ");
                permissions.add("PATIENT_VISIT_CREATE");
                permissions.add("PATIENT_VISIT_READ");
                permissions.add("CLINICAL_HISTORY_READ");
                permissions.add("MEDICATION_APPLY");
                permissions.add("PROCEDURE_EXECUTE");
                break;
                
            case DOCTOR:
                // Acceso completo a información médica
                permissions.add("PATIENT_READ");
                permissions.add("CLINICAL_HISTORY_CREATE");
                permissions.add("CLINICAL_HISTORY_READ");
                permissions.add("CLINICAL_HISTORY_UPDATE");
                permissions.add("MEDICAL_ORDER_CREATE");
                permissions.add("MEDICATION_PRESCRIBE");
                permissions.add("PROCEDURE_ORDER");
                permissions.add("DIAGNOSTIC_ORDER");
                permissions.add("VITAL_SIGNS_READ");
                permissions.add("PATIENT_VISIT_READ");
                break;
                
            case PATIENT:
                // Solo acceso a su propia información
                permissions.add("OWN_CLINICAL_HISTORY_READ");
                permissions.add("OWN_INVOICE_READ");
                permissions.add("OWN_ORDERS_READ");
                permissions.add("OWN_PROFILE_UPDATE");
                break;
                
            default:
                // Sin permisos por defecto
                break;
        }
        
        return permissions;
    }
    
    /**
     * Verificar permiso específico del usuario
     */
    private boolean checkSpecificPermission(AuthenticatedUser user, String requiredPermission) {
        List<String> userPermissions = generatePermissionsList(user);
        return userPermissions.contains(requiredPermission);
    }
    
    /**
     * Verificar acceso a recurso específico según rol y acción
     */
    private boolean checkResourceAccess(AuthenticatedUser user, String resourceType, String action) {
        String permission = resourceType + "_" + action;
        return checkSpecificPermission(user, permission);
    }
    
    /**
     * Generar token de sesión usando JWT
     */
    private String generateSessionToken(AuthenticatedUser user) {
        try {
            // TODO: Inyectar JwtTokenProvider y usar para generar token real
            // Por ahora, generar token simulado para testing
            return "JWT_" + user.getIdCard() + "_" + System.currentTimeMillis();
        } catch (Exception e) {
            logger.error("Error generating session token: " + e.getMessage());
            return "ERROR_TOKEN";
        }
    }
    
    /**
     * Obtener usuario actual desde cache/sesión (simulado)
     */
    private Optional<AuthenticatedUser> getCurrentUser(String userId) {
        // En implementación real: obtener de cache Redis, base de datos de sesiones, etc.
        // Por ahora retornamos vacío para simular sesión no encontrada
        return Optional.empty();
    }
    
    /**
     * Invalidar sesión del usuario
     */
    private void invalidateUserSession(String userId) {
        // En implementación real: 
        // - Invalidar tokens JWT
        // - Limpiar cache de sesión
        // - Marcar sesión como inválida en BD
        System.out.println("Session invalidated for user: " + userId);
    }
    
    // =============================================================================
    // MÉTODOS DE LOGGING Y AUDITORÍA
    // =============================================================================
    
    private void logSuccessfulLogin(AuthenticatedUser user) {
        // En implementación real: log a archivo, base de datos de auditoría, etc.
        System.out.printf("SUCCESSFUL LOGIN: User %s (%s) role %s at %s%n", 
            user.getFullName(), user.getIdCard(), user.getRole(), 
            java.time.LocalDateTime.now());
    }
    
    private void logFailedLogin(String username, String reason) {
        // En implementación real: log para detectar ataques de fuerza bruta
        System.out.printf("FAILED LOGIN: Username %s - Reason: %s at %s%n", 
            username, reason, java.time.LocalDateTime.now());
    }
    
    private void logSuccessfulLogout(String userId) {
        System.out.printf("SUCCESSFUL LOGOUT: User %s at %s%n", 
            userId, java.time.LocalDateTime.now());
    }
    
    private void logSystemError(String operation, Exception e) {
        // En implementación real: log detallado con stack trace
        System.err.printf("SYSTEM ERROR in %s: %s at %s%n", 
            operation, e.getMessage(), java.time.LocalDateTime.now());
    }
    
    // =============================================================================
    // MÉTODOS DE UTILIDAD PARA TESTING
    // =============================================================================
    
    /**
     * Crear request de login de prueba
     */
    public LoginRequest createTestLoginRequest(String username, String password) {
        return new LoginRequest(username, password);
    }
    
    /**
     * Validar estructura de permisos por rol
     */
    public boolean validateRolePermissions(String roleName) {
        try {
            // Crear usuario ficticio para probar generación de permisos
            AuthenticatedUser testUser = new AuthenticatedUser(
                "TEST123", "Test User", 
                app.domain.model.Role.valueOf(roleName), true
            );
            
            List<String> permissions = generatePermissionsList(testUser);
            
            // Validar que se generaron permisos
            return !permissions.isEmpty();
            
        } catch (Exception e) {
            return false;
        }
    }
}