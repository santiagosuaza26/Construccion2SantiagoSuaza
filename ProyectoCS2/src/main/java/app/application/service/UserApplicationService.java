package app.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import app.application.dto.request.CreateUserRequest;
import app.application.dto.response.CommonResponse;
import app.application.dto.response.UserResponse;
import app.application.mapper.UserMapper;
import app.domain.model.Role;
import app.domain.model.User;
import app.domain.port.UserRepository;
import app.domain.services.AuthenticationService.AuthenticatedUser;
import app.domain.services.HumanResourcesService;

/**
 * UserApplicationService - Servicio de aplicación para gestión de usuarios
 * 
 * CASOS DE USO IMPLEMENTADOS:
 * - Crear usuario del sistema (solo HR)
 * - Actualizar información de usuario (solo HR)
 * - Eliminar usuario del sistema (solo HR)
 * - Listar todos los usuarios (solo HR)
 * - Consultar usuario por ID (solo HR)
 * - Consultar usuario por username (solo HR)
 * 
 * REGLAS DE NEGOCIO CRÍTICAS:
 * - SOLO personal de Recursos Humanos puede usar este servicio
 * - NO pueden crear usuarios con rol PATIENT (usar PatientApplicationService)
 * - Validar que cédula y username sean únicos
 * - Aplicar todas las validaciones del documento
 * 
 * RESTRICCIONES DE SEGURIDAD:
 * - Verificación de rol HR en cada operación
 * - Validación de permisos específicos
 * - Auditoría completa de operaciones
 * - Manejo seguro de credenciales
 */
@Service
public class UserApplicationService {
    
    private final HumanResourcesService humanResourcesService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    public UserApplicationService(HumanResourcesService humanResourcesService,
                                UserRepository userRepository,
                                UserMapper userMapper) {
        this.humanResourcesService = humanResourcesService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    
    /**
     * CASO DE USO: Crear Usuario del Sistema (Versión básica sin autenticación)
     *
     * Crea un nuevo usuario en el sistema.
     * NOTA: En producción requerir autenticación HR
     *
     * @param request Datos del usuario a crear
     * @return Response con información del usuario creado
     */
    public CommonResponse<UserResponse> createUser(CreateUserRequest request) {
        try {
            // 2. Validar el request
            validateCreateUserRequest(request);

            // 3. Verificar unicidad de cédula y username
            CommonResponse<Void> uniquenessCheck = validateUniqueness(request);
            if (!uniquenessCheck.isSuccess()) {
                return CommonResponse.error(uniquenessCheck.getMessage(), uniquenessCheck.getErrorCode());
            }

            // 4. Convertir DTO a Domain model usando mapper
            User userToCreate = userMapper.toUser(request);

            // 5. Crear usuario usando el servicio de dominio
            User createdUser = humanResourcesService.createUser(userToCreate);

            // 6. Convertir resultado a DTO response
            UserResponse userResponse = userMapper.toResponse(createdUser);

            return CommonResponse.success("User created successfully", userResponse);

        } catch (IllegalArgumentException e) {
            // Error de validación de dominio o mapper
            return CommonResponse.error(e.getMessage(), "USER_002");

        } catch (Exception e) {
            // Error interno del sistema
            return CommonResponse.error("Internal error creating user", "USER_003");
        }
    }

    /**
     * CASO DE USO: Crear Usuario del Sistema
     *
     * Solo personal de Recursos Humanos puede crear nuevos usuarios del sistema.
     * Valida unicidad de cédula y username, aplica todas las reglas de negocio.
     *
     * @param request Datos del usuario a crear
     * @param currentUser Usuario autenticado (debe ser HR)
     * @return Response con información del usuario creado
     */
    public CommonResponse<UserResponse> createUser(CreateUserRequest request, AuthenticatedUser currentUser) {
        try {
            // 1. Validar que solo HR puede crear usuarios
            if (!canCreateUsers(currentUser)) {
                logUnauthorizedAccess(currentUser, "CREATE_USER");
                return CommonResponse.error("Access denied - Only Human Resources can create users", "USER_001");
            }
            
            // 2. Validar el request
            validateCreateUserRequest(request);
            
            // 3. Verificar unicidad de cédula y username
            CommonResponse<Void> uniquenessCheck = validateUniqueness(request);
            if (!uniquenessCheck.isSuccess()) {
                return CommonResponse.error(uniquenessCheck.getMessage(), uniquenessCheck.getErrorCode());
            }
            
            // 4. Convertir DTO a Domain model usando mapper
            User userToCreate = userMapper.toUser(request);
            
            // 5. Crear usuario usando el servicio de dominio
            User createdUser = humanResourcesService.createUser(userToCreate);
            
            // 6. Convertir resultado a DTO response
            UserResponse userResponse = userMapper.toResponse(createdUser);
            
            // 7. Log de auditoría
            logUserCreated(createdUser, currentUser);
            
            return CommonResponse.success("User created successfully", userResponse);
            
        } catch (IllegalArgumentException e) {
            // Error de validación de dominio o mapper
            logValidationError("createUser", e, currentUser);
            return CommonResponse.error(e.getMessage(), "USER_002");
            
        } catch (Exception e) {
            // Error interno del sistema
            logSystemError("createUser", e, currentUser);
            return CommonResponse.error("Internal error creating user", "USER_003");
        }
    }
    
    /**
     * CASO DE USO: Actualizar Usuario (Versión básica sin autenticación)
     *
     * Permite actualizar información de un usuario existente.
     * NOTA: En producción requerir autenticación HR
     *
     * @param userIdCard Cédula del usuario a actualizar
     * @param request Nuevos datos del usuario
     * @return Response con información actualizada
     */
    public CommonResponse<UserResponse> updateUser(String userIdCard, CreateUserRequest request) {
        try {
            // 2. Validar parámetros
            if (userIdCard == null || userIdCard.isBlank()) {
                return CommonResponse.error("User ID card is required", "USER_005");
            }

            validateCreateUserRequest(request);

            // 3. Verificar que el usuario existe
            Optional<User> existingUserOpt = userRepository.findByIdCard(userIdCard);
            if (existingUserOpt.isEmpty()) {
                return CommonResponse.error("User not found with ID: " + userIdCard, "USER_006");
            }

            User existingUser = existingUserOpt.get();

            // 4. Validar unicidad si cambió cédula o username
            if (!userIdCard.equals(request.getIdCard()) ||
                !existingUser.getCredentials().getUsername().equals(request.getUsername())) {
                CommonResponse<Void> uniquenessCheck = validateUniquenessForUpdate(request, userIdCard);
                if (!uniquenessCheck.isSuccess()) {
                    return CommonResponse.error(uniquenessCheck.getMessage(), uniquenessCheck.getErrorCode());
                }
            }

            // 5. Convertir y actualizar
            User updatedUserData = userMapper.toUser(request);

            // En implementación real: usar un método update específico
            // Por ahora simulamos eliminando y creando
            humanResourcesService.deleteUser(userIdCard);
            User updatedUser = humanResourcesService.createUser(updatedUserData);

            UserResponse userResponse = userMapper.toResponse(updatedUser);

            return CommonResponse.success("User updated successfully", userResponse);

        } catch (IllegalArgumentException e) {
            return CommonResponse.error(e.getMessage(), "USER_007");

        } catch (Exception e) {
            return CommonResponse.error("Internal error updating user", "USER_008");
        }
    }

    /**
     * CASO DE USO: Actualizar Usuario
     *
     * Permite actualizar información de un usuario existente.
     * Solo HR puede realizar esta operación.
     *
     * @param userIdCard Cédula del usuario a actualizar
     * @param request Nuevos datos del usuario
     * @param currentUser Usuario autenticado (debe ser HR)
     * @return Response con información actualizada
     */
    public CommonResponse<UserResponse> updateUser(String userIdCard, CreateUserRequest request,
                                                    AuthenticatedUser currentUser) {
        try {
            // 1. Validar permisos
            if (!canUpdateUsers(currentUser)) {
                logUnauthorizedAccess(currentUser, "UPDATE_USER");
                return CommonResponse.error("Access denied - Only Human Resources can update users", "USER_004");
            }
            
            // 2. Validar parámetros
            if (userIdCard == null || userIdCard.isBlank()) {
                return CommonResponse.error("User ID card is required", "USER_005");
            }
            
            validateCreateUserRequest(request);
            
            // 3. Verificar que el usuario existe
            Optional<User> existingUserOpt = userRepository.findByIdCard(userIdCard);
            if (existingUserOpt.isEmpty()) {
                return CommonResponse.error("User not found with ID: " + userIdCard, "USER_006");
            }
            
            User existingUser = existingUserOpt.get();
            
            // 4. Validar unicidad si cambió cédula o username
            if (!userIdCard.equals(request.getIdCard()) || 
                !existingUser.getCredentials().getUsername().equals(request.getUsername())) {
                CommonResponse<Void> uniquenessCheck = validateUniquenessForUpdate(request, userIdCard);
                if (!uniquenessCheck.isSuccess()) {
                    return CommonResponse.error(uniquenessCheck.getMessage(), uniquenessCheck.getErrorCode());
                }
            }
            
            // 5. Convertir y actualizar
            User updatedUserData = userMapper.toUser(request);
            
            // En implementación real: usar un método update específico
            // Por ahora simulamos eliminando y creando
            humanResourcesService.deleteUser(userIdCard);
            User updatedUser = humanResourcesService.createUser(updatedUserData);
            
            UserResponse userResponse = userMapper.toResponse(updatedUser);
            
            // 6. Log de auditoría
            logUserUpdated(existingUser, updatedUser, currentUser);
            
            return CommonResponse.success("User updated successfully", userResponse);
            
        } catch (IllegalArgumentException e) {
            logValidationError("updateUser", e, currentUser);
            return CommonResponse.error(e.getMessage(), "USER_007");
            
        } catch (Exception e) {
            logSystemError("updateUser", e, currentUser);
            return CommonResponse.error("Internal error updating user", "USER_008");
        }
    }
    
    /**
     * CASO DE USO: Eliminar Usuario (Versión básica sin autenticación)
     *
     * Elimina un usuario del sistema.
     * NOTA: En producción requerir autenticación HR
     *
     * @param userIdCard Cédula del usuario a eliminar
     * @return Response confirmando la eliminación
     */
    public CommonResponse<String> deleteUser(String userIdCard) {
        try {
            // 2. Validar parámetros
            if (userIdCard == null || userIdCard.isBlank()) {
                return CommonResponse.error("User ID card is required", "USER_010");
            }

            // 3. Verificar que el usuario existe
            Optional<User> userToDeleteOpt = userRepository.findByIdCard(userIdCard);
            if (userToDeleteOpt.isEmpty()) {
                return CommonResponse.error("User not found with ID: " + userIdCard, "USER_011");
            }

            User userToDelete = userToDeleteOpt.get();

            // 5. Eliminar usando servicio de dominio
            humanResourcesService.deleteUser(userIdCard);

            return CommonResponse.success("User deleted successfully: " + userToDelete.getFullName());

        } catch (Exception e) {
            return CommonResponse.error("Internal error deleting user", "USER_013");
        }
    }

    /**
     * CASO DE USO: Eliminar Usuario
     *
     * Elimina un usuario del sistema. Solo HR puede realizar esta operación.
     * Incluye validaciones de seguridad y auditoría.
     *
     * @param userIdCard Cédula del usuario a eliminar
     * @param currentUser Usuario autenticado (debe ser HR)
     * @return Response confirmando la eliminación
     */
    public CommonResponse<String> deleteUser(String userIdCard, AuthenticatedUser currentUser) {
        try {
            // 1. Validar permisos
            if (!canDeleteUsers(currentUser)) {
                logUnauthorizedAccess(currentUser, "DELETE_USER");
                return CommonResponse.error("Access denied - Only Human Resources can delete users", "USER_009");
            }
            
            // 2. Validar parámetros
            if (userIdCard == null || userIdCard.isBlank()) {
                return CommonResponse.error("User ID card is required", "USER_010");
            }
            
            // 3. Verificar que el usuario existe
            Optional<User> userToDeleteOpt = userRepository.findByIdCard(userIdCard);
            if (userToDeleteOpt.isEmpty()) {
                return CommonResponse.error("User not found with ID: " + userIdCard, "USER_011");
            }
            
            User userToDelete = userToDeleteOpt.get();
            
            // 4. Validaciones adicionales de seguridad
            if (!canDeleteSpecificUser(userToDelete, currentUser)) {
                return CommonResponse.error("Cannot delete this user due to security restrictions", "USER_012");
            }
            
            // 5. Eliminar usando servicio de dominio
            humanResourcesService.deleteUser(userIdCard);
            
            // 6. Log de auditoría
            logUserDeleted(userToDelete, currentUser);
            
            return CommonResponse.success("User deleted successfully: " + userToDelete.getFullName());
            
        } catch (Exception e) {
            logSystemError("deleteUser", e, currentUser);
            return CommonResponse.error("Internal error deleting user", "USER_013");
        }
    }
    
    /**
     * CASO DE USO: Listar Todos los Usuarios (Versión básica sin autenticación)
     *
     * Retorna lista completa de usuarios del sistema.
     * NOTA: En producción requerir autenticación HR
     *
     * @return Response con lista de usuarios
     */
    public CommonResponse<List<UserResponse>> getAllUsers() {
        try {
            // 2. Obtener todos los usuarios
            List<User> users = userRepository.findAll();

            // 3. Convertir a DTOs
            List<UserResponse> userResponses = userMapper.toResponseList(users);

            return CommonResponse.success(
                String.format("Retrieved %d users", users.size()),
                userResponses
            );

        } catch (Exception e) {
            logSystemError("getAllUsers", e, null);
            return CommonResponse.error("Internal error retrieving users", "USER_015");
        }
    }

    /**
     * CASO DE USO: Listar Todos los Usuarios
     *
     * Retorna lista completa de usuarios del sistema.
     * Solo HR puede ver esta información.
     *
     * @param currentUser Usuario autenticado (debe ser HR)
     * @return Response con lista de usuarios
     */
    public CommonResponse<List<UserResponse>> getAllUsers(AuthenticatedUser currentUser) {
        try {
            // 1. Validar permisos
            if (!canViewUsers(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_ALL_USERS");
                return CommonResponse.error("Access denied - Only Human Resources can view users list", "USER_014");
            }
            
            // 2. Obtener todos los usuarios
            List<User> users = userRepository.findAll();
            
            // 3. Convertir a DTOs
            List<UserResponse> userResponses = userMapper.toResponseList(users);
            
            // 4. Log de auditoría
            logUsersListed(users.size(), currentUser);
            
            return CommonResponse.success(
                String.format("Retrieved %d users", users.size()), 
                userResponses
            );
            
        } catch (Exception e) {
            logSystemError("getAllUsers", e, currentUser);
            return CommonResponse.error("Internal error retrieving users", "USER_015");
        }
    }
    
    /**
     * CASO DE USO: Consultar Usuario por ID (Versión básica sin autenticación)
     *
     * Busca un usuario específico por su cédula.
     * NOTA: En producción requerir autenticación HR
     *
     * @param userIdCard Cédula del usuario a buscar
     * @return Response con información del usuario
     */
    public CommonResponse<UserResponse> getUserByIdCard(String userIdCard) {
        try {
            // 2. Validar parámetros
            if (userIdCard == null || userIdCard.isBlank()) {
                return CommonResponse.error("User ID card is required", "USER_017");
            }

            // 3. Buscar usuario
            Optional<User> userOpt = userRepository.findByIdCard(userIdCard);

            if (userOpt.isEmpty()) {
                return CommonResponse.error("User not found with ID: " + userIdCard, "USER_018");
            }

            // 4. Convertir a DTO
            UserResponse userResponse = userMapper.toResponse(userOpt.get());

            return CommonResponse.success("User found", userResponse);

        } catch (Exception e) {
            logSystemError("getUserByIdCard", e, null);
            return CommonResponse.error("Internal error retrieving user", "USER_019");
        }
    }

    /**
     * CASO DE USO: Consultar Usuario por ID
     *
     * Busca un usuario específico por su cédula.
     * Solo HR puede acceder a esta información.
     *
     * @param userIdCard Cédula del usuario a buscar
     * @param currentUser Usuario autenticado (debe ser HR)
     * @return Response con información del usuario
     */
    public CommonResponse<UserResponse> getUserById(String userIdCard, AuthenticatedUser currentUser) {
        try {
            // 1. Validar permisos
            if (!canViewUsers(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_USER_BY_ID");
                return CommonResponse.error("Access denied - Only Human Resources can view user details", "USER_016");
            }
            
            // 2. Validar parámetros
            if (userIdCard == null || userIdCard.isBlank()) {
                return CommonResponse.error("User ID card is required", "USER_017");
            }
            
            // 3. Buscar usuario
            Optional<User> userOpt = userRepository.findByIdCard(userIdCard);
            
            if (userOpt.isEmpty()) {
                return CommonResponse.error("User not found with ID: " + userIdCard, "USER_018");
            }
            
            // 4. Convertir a DTO
            UserResponse userResponse = userMapper.toResponse(userOpt.get());
            
            // 5. Log de auditoría
            logUserViewed(userOpt.get(), currentUser);
            
            return CommonResponse.success("User found", userResponse);
            
        } catch (Exception e) {
            logSystemError("getUserById", e, currentUser);
            return CommonResponse.error("Internal error retrieving user", "USER_019");
        }
    }
    
    /**
     * CASO DE USO: Consultar Usuario por Username
     * 
     * Busca un usuario por su nombre de usuario.
     * Solo HR puede acceder a esta información.
     */
    public CommonResponse<UserResponse> getUserByUsername(String username, AuthenticatedUser currentUser) {
        try {
            // 1. Validar permisos
            if (!canViewUsers(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_USER_BY_USERNAME");
                return CommonResponse.error("Access denied - Only Human Resources can view user details", "USER_020");
            }
            
            // 2. Validar parámetros
            if (username == null || username.isBlank()) {
                return CommonResponse.error("Username is required", "USER_021");
            }
            
            // 3. Buscar usuario
            Optional<User> userOpt = userRepository.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                return CommonResponse.error("User not found with username: " + username, "USER_022");
            }
            
            // 4. Convertir a DTO
            UserResponse userResponse = userMapper.toResponse(userOpt.get());
            
            // 5. Log de auditoría
            logUserViewed(userOpt.get(), currentUser);
            
            return CommonResponse.success("User found", userResponse);
            
        } catch (Exception e) {
            logSystemError("getUserByUsername", e, currentUser);
            return CommonResponse.error("Internal error retrieving user", "USER_023");
        }
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE VALIDACIÓN
    // =============================================================================
    
    private void validateCreateUserRequest(CreateUserRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("User request cannot be null");
        }
        
        // Usar validación adicional del mapper
        userMapper.validateCreateUserRequest(request);
        
        // Validación específica: no permitir crear usuarios PATIENT
        if ("PATIENT".equalsIgnoreCase(request.getRole())) {
            throw new IllegalArgumentException("Cannot create PATIENT users through this service - use Patient registration");
        }
    }
    
    private CommonResponse<Void> validateUniqueness(CreateUserRequest request) {
        // Verificar cédula única
        if (userRepository.existsByIdCard(request.getIdCard())) {
            return CommonResponse.error("User with ID card " + request.getIdCard() + " already exists", "USER_024");
        }
        
        // Verificar username único
        if (userRepository.existsByUsername(request.getUsername())) {
            return CommonResponse.error("Username " + request.getUsername() + " already exists", "USER_025");
        }
        
        return CommonResponse.success(null);
    }
    
    private CommonResponse<Void> validateUniquenessForUpdate(CreateUserRequest request, String currentIdCard) {
        // Verificar cédula única (excluyendo el usuario actual)
        Optional<User> userWithSameId = userRepository.findByIdCard(request.getIdCard());
        if (userWithSameId.isPresent() && !userWithSameId.get().getIdCard().equals(currentIdCard)) {
            return CommonResponse.error("User with ID card " + request.getIdCard() + " already exists", "USER_026");
        }
        
        // Verificar username único (excluyendo el usuario actual)
        Optional<User> userWithSameUsername = userRepository.findByUsername(request.getUsername());
        if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getIdCard().equals(currentIdCard)) {
            return CommonResponse.error("Username " + request.getUsername() + " already exists", "USER_027");
        }
        
        return CommonResponse.success(null);
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE AUTORIZACIÓN
    // =============================================================================
    
    private boolean canCreateUsers(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.HUMAN_RESOURCES && user.canCreateUsers();
    }
    
    private boolean canUpdateUsers(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.HUMAN_RESOURCES;
    }
    
    private boolean canDeleteUsers(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.HUMAN_RESOURCES;
    }
    
    private boolean canViewUsers(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.HUMAN_RESOURCES;
    }
    
    private boolean canDeleteSpecificUser(User userToDelete, AuthenticatedUser currentUser) {
        // Validaciones adicionales de seguridad
        
        // No permitir auto-eliminación
        if (userToDelete.getIdCard().equals(currentUser.getIdCard())) {
            return false;
        }
        
        // En implementación real: otras validaciones como:
        // - No eliminar usuarios con sesiones activas
        // - No eliminar usuarios con datos críticos pendientes
        // - Validar jerarquías organizacionales
        
        return true;
    }
    
    // =============================================================================
    // MÉTODOS DE LOGGING Y AUDITORÍA
    // =============================================================================
    
    private void logUserCreated(User createdUser, AuthenticatedUser currentUser) {
        System.out.printf("USER CREATED: %s created user %s (%s) with role %s at %s%n",
            currentUser.getFullName(), createdUser.getFullName(), createdUser.getIdCard(),
            createdUser.getRole(), java.time.LocalDateTime.now());
    }
    
    private void logUserUpdated(User oldUser, User updatedUser, AuthenticatedUser currentUser) {
        System.out.printf("USER UPDATED: %s updated user %s (%s) at %s%n",
            currentUser.getFullName(), updatedUser.getFullName(), updatedUser.getIdCard(),
            java.time.LocalDateTime.now());
    }
    
    private void logUserDeleted(User deletedUser, AuthenticatedUser currentUser) {
        System.out.printf("USER DELETED: %s deleted user %s (%s) at %s%n",
            currentUser.getFullName(), deletedUser.getFullName(), deletedUser.getIdCard(),
            java.time.LocalDateTime.now());
    }
    
    private void logUsersListed(int count, AuthenticatedUser currentUser) {
        System.out.printf("USERS LISTED: %s retrieved %d users at %s%n",
            currentUser.getFullName(), count, java.time.LocalDateTime.now());
    }
    
    private void logUserViewed(User viewedUser, AuthenticatedUser currentUser) {
        System.out.printf("USER VIEWED: %s viewed user %s (%s) at %s%n",
            currentUser.getFullName(), viewedUser.getFullName(), viewedUser.getIdCard(),
            java.time.LocalDateTime.now());
    }
    
    private void logUnauthorizedAccess(AuthenticatedUser user, String operation) {
        System.err.printf("UNAUTHORIZED ACCESS: User %s (%s) role %s attempted %s at %s%n",
            user.getFullName(), user.getIdCard(), user.getRole(), operation,
            java.time.LocalDateTime.now());
    }
    
    private void logValidationError(String operation, Exception e, AuthenticatedUser user) {
        System.err.printf("VALIDATION ERROR in %s by %s: %s at %s%n",
            operation, user.getFullName(), e.getMessage(), java.time.LocalDateTime.now());
    }
    
    private void logSystemError(String operation, Exception e, AuthenticatedUser user) {
        System.err.printf("SYSTEM ERROR in %s by %s: %s at %s%n",
            operation, user.getFullName(), e.getMessage(), java.time.LocalDateTime.now());
    }
}