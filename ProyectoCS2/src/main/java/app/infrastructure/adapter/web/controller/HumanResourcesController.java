package app.infrastructure.adapter.web.controller;

import app.application.dto.request.CreateUserRequest;
import app.application.dto.response.CommonResponse;
import app.application.dto.response.UserResponse;
import app.application.service.UserApplicationService;
import app.domain.model.Role;
import app.domain.services.AuthenticationService.AuthenticatedUser;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para funcionalidades de Recursos Humanos
 *
 * Endpoints exclusivos para Recursos Humanos:
 * - GET /api/hr/users: Listar todos los usuarios del sistema
 * - GET /api/hr/users/{idCard}: Obtener usuario específico
 * - POST /api/hr/users: Crear nuevo usuario
 * - PUT /api/hr/users/{idCard}: Actualizar usuario
 * - DELETE /api/hr/users/{idCard}: Eliminar usuario
 * - GET /api/hr/roles: Listar roles disponibles
 * - GET /api/hr/permissions: Verificar permisos del rol actual
 *
 * RESTRICCIONES:
 * - NO puede acceder a información de pacientes
 * - NO puede acceder a información médica
 * - NO puede gestionar inventarios
 * - NO puede generar facturas
 */
@RestController
@RequestMapping("/api/hr")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class HumanResourcesController {

    private final UserApplicationService userApplicationService;

    public HumanResourcesController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    /**
     * Listar todos los usuarios del sistema
     * Exclusivo para Recursos Humanos
     */
    @GetMapping("/users")
    public ResponseEntity<CommonResponse<List<UserResponse>>> getAllUsers(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "HUMAN_RESOURCES");

            if (!currentUser.canCreateUsers()) {
                CommonResponse<List<UserResponse>> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Recursos Humanos puede ver usuarios", "HR_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<List<UserResponse>> response = userApplicationService.getAllUsers();

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<List<UserResponse>> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener usuarios", "HR_USERS_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtener usuario específico por ID
     * Exclusivo para Recursos Humanos
     */
    @GetMapping("/users/{idCard}")
    public ResponseEntity<CommonResponse<UserResponse>> getUserById(
            @PathVariable String idCard,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (idCard == null || idCard.trim().isEmpty()) {
                CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                    "Cédula del usuario es requerida", "USER_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "HUMAN_RESOURCES");

            if (!currentUser.canCreateUsers()) {
                CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Recursos Humanos puede ver usuarios", "HR_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<UserResponse> response = userApplicationService.getUserByIdCard(idCard.trim());

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al buscar usuario", "HR_USER_SEARCH_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Crear nuevo usuario
     * Exclusivo para Recursos Humanos
     */
    @PostMapping("/users")
    public ResponseEntity<CommonResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequest,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "HUMAN_RESOURCES");

            if (!currentUser.canCreateUsers()) {
                CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Recursos Humanos puede crear usuarios", "HR_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            // Validar que no se esté creando un paciente (pacientes usan Patient entity)
            if (createUserRequest.getRole() != null &&
                "PATIENT".equals(createUserRequest.getRole().toString())) {
                CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                    "No se puede crear pacientes usando este endpoint. Use el endpoint de registro de pacientes.",
                    "HR_PATIENT_CREATION_DENIED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            CommonResponse<UserResponse> response = userApplicationService.createUser(createUserRequest);

            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al crear usuario", "HR_USER_CREATE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Actualizar usuario existente
     * Exclusivo para Recursos Humanos
     */
    @PutMapping("/users/{idCard}")
    public ResponseEntity<CommonResponse<UserResponse>> updateUser(
            @PathVariable String idCard,
            @Valid @RequestBody CreateUserRequest updateUserRequest,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (idCard == null || idCard.trim().isEmpty()) {
                CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                    "Cédula del usuario es requerida", "USER_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "HUMAN_RESOURCES");

            if (!currentUser.canCreateUsers()) {
                CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Recursos Humanos puede actualizar usuarios", "HR_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<UserResponse> response = userApplicationService.updateUser(idCard.trim(), updateUserRequest);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al actualizar usuario", "HR_USER_UPDATE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Eliminar usuario
     * Exclusivo para Recursos Humanos
     */
    @DeleteMapping("/users/{idCard}")
    public ResponseEntity<CommonResponse<String>> deleteUser(
            @PathVariable String idCard,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (idCard == null || idCard.trim().isEmpty()) {
                CommonResponse<String> errorResponse = CommonResponse.error(
                    "Cédula del usuario es requerida", "USER_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "HUMAN_RESOURCES");

            if (!currentUser.canCreateUsers()) {
                CommonResponse<String> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Recursos Humanos puede eliminar usuarios", "HR_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<String> response = userApplicationService.deleteUser(idCard.trim());

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<String> errorResponse = CommonResponse.error(
                "Error interno del servidor al eliminar usuario", "HR_USER_DELETE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Listar roles disponibles
     * Exclusivo para Recursos Humanos
     */
    @GetMapping("/roles")
    public ResponseEntity<CommonResponse<List<String>>> getAvailableRoles(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "HUMAN_RESOURCES");

            if (!currentUser.canCreateUsers()) {
                CommonResponse<List<String>> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Recursos Humanos puede ver roles", "HR_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            // Excluir el rol PATIENT ya que se maneja por separado
            List<String> roles = List.of("HUMAN_RESOURCES", "ADMINISTRATIVE", "SUPPORT", "NURSE", "DOCTOR");

            CommonResponse<List<String>> response = CommonResponse.success(
                "Roles disponibles obtenidos exitosamente", roles);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            CommonResponse<List<String>> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener roles", "HR_ROLES_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Verificar permisos del usuario actual
     * Exclusivo para Recursos Humanos
     */
    @GetMapping("/permissions")
    public ResponseEntity<CommonResponse<List<String>>> getCurrentUserPermissions(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "HUMAN_RESOURCES");

            if (!currentUser.canCreateUsers()) {
                CommonResponse<List<String>> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Recursos Humanos puede ver permisos", "HR_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            // Generar lista de permisos según el rol
            List<String> permissions = generateHRPermissions();

            CommonResponse<List<String>> response = CommonResponse.success(
                "Permisos del usuario obtenidos exitosamente", permissions);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            CommonResponse<List<String>> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener permisos", "HR_PERMISSIONS_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Método temporal para crear usuario autenticado mock
     * TODO: Reemplazar con implementación real de JWT
     */
    private AuthenticatedUser createMockAuthenticatedUser(String userId, String roleString) {
        if (userId == null) {
            return new AuthenticatedUser("12345678", "Mock HR User", Role.valueOf(roleString), true);
        }
        return new AuthenticatedUser(userId, "Mock HR User", Role.valueOf(roleString), true);
    }

    /**
     * Generar lista de permisos específicos para Recursos Humanos
     */
    private List<String> generateHRPermissions() {
        return List.of(
            "USER_CREATE",
            "USER_READ",
            "USER_UPDATE",
            "USER_DELETE",
            "ROLE_ASSIGN",
            "PERMISSION_VIEW"
        );
    }
}