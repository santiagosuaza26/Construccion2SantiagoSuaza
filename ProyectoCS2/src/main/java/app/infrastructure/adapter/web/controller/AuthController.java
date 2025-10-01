package app.infrastructure.adapter.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.application.dto.request.LoginRequest;
import app.application.dto.response.AuthResponse;
import app.application.dto.response.CommonResponse;
import app.application.service.AuthApplicationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class AuthController {

    // Constantes para códigos de error (resuelve problemas de SonarQube)
    private static final String ERROR_CODE_UNAUTHORIZED = "AUTH_401";
    private static final String ERROR_CODE_SERVER_ERROR = "AUTH_500";
    private static final String ERROR_CODE_LOGOUT_SERVER_ERROR = "LOGOUT_500";
    private static final String ERROR_CODE_ME_SERVER_ERROR = "ME_500";
    private static final String ERROR_CODE_PERMISSIONS_SERVER_ERROR = "PERM_500";
    private static final String ERROR_CODE_ACCESS_SERVER_ERROR = "ACCESS_500";
    private static final String ERROR_CODE_HEALTH_ERROR = "HEALTH_ERROR";

    // Constantes para mensajes de error
    private static final String MESSAGE_USER_NOT_AUTHENTICATED = "Usuario no autenticado";
    private static final String MESSAGE_SERVER_ERROR_LOGOUT = "Error interno del servidor durante logout";
    private static final String MESSAGE_SERVER_ERROR_ME = "Error interno del servidor";
    private static final String MESSAGE_SERVER_ERROR_PERMISSIONS = "Error verificando permisos";
    private static final String MESSAGE_SERVER_ERROR_ACCESS = "Error verificando acceso a recurso";

    private final AuthApplicationService authApplicationService;

    public AuthController(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    /**
     * Valida si el usuario está autenticado
     */
    private boolean isUserAuthenticated(String userId) {
        return userId != null;
    }

    /**
     * Crea una respuesta de error de autenticación
     */
    private <T> ResponseEntity<CommonResponse<T>> createUnauthorizedResponse() {
        CommonResponse<T> errorResponse = CommonResponse.error(
            MESSAGE_USER_NOT_AUTHENTICATED, ERROR_CODE_UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * Maneja errores internos del servidor de forma genérica
     */
    private <T> ResponseEntity<CommonResponse<T>> handleServerError(String message, String errorCode, Exception e) {
        System.err.println("ERROR AuthController - " + message + ": " + e.getMessage());
        CommonResponse<T> errorResponse = CommonResponse.error(message, errorCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        try {
            CommonResponse<AuthResponse> response = authApplicationService.login(loginRequest);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            return handleServerError("Error interno del servidor durante autenticación",
                ERROR_CODE_SERVER_ERROR, e);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<String>> logout(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            String actualUserId = userId != null ? userId : "unknown";
            CommonResponse<String> response = authApplicationService.logout(actualUserId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleServerError(MESSAGE_SERVER_ERROR_LOGOUT,
                ERROR_CODE_LOGOUT_SERVER_ERROR, e);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<AuthResponse>> getCurrentUser(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        if (!isUserAuthenticated(userId)) {
            return createUnauthorizedResponse();
        }

        try {
            CommonResponse<AuthResponse> response = authApplicationService.getCurrentUserInfo(userId);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            return handleServerError(MESSAGE_SERVER_ERROR_ME, ERROR_CODE_ME_SERVER_ERROR, e);
        }
    }

    @GetMapping("/permissions")
    public ResponseEntity<CommonResponse<Boolean>> checkPermission(
            @RequestHeader(value = "User-ID", required = false) String userId,
            @RequestParam String permission) {

        if (!isUserAuthenticated(userId)) {
            return createUnauthorizedResponse();
        }

        try {
            CommonResponse<Boolean> response = authApplicationService.hasPermission(userId, permission);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleServerError(MESSAGE_SERVER_ERROR_PERMISSIONS,
                ERROR_CODE_PERMISSIONS_SERVER_ERROR, e);
        }
    }

    @GetMapping("/access")
    public ResponseEntity<CommonResponse<Boolean>> checkResourceAccess(
            @RequestHeader(value = "User-ID", required = false) String userId,
            @RequestParam String resourceType,
            @RequestParam String action) {

        if (!isUserAuthenticated(userId)) {
            return createUnauthorizedResponse();
        }

        try {
            CommonResponse<Boolean> response = authApplicationService.canAccessResource(
                userId, resourceType, action);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleServerError(MESSAGE_SERVER_ERROR_ACCESS,
                ERROR_CODE_ACCESS_SERVER_ERROR, e);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<CommonResponse<String>> healthCheck() {
        try {
            CommonResponse<String> response = CommonResponse.success(
                "Application is running correctly at " + System.currentTimeMillis(),
                "HEALTH_OK"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleServerError("Health check failed: " + e.getMessage(),
                ERROR_CODE_HEALTH_ERROR, e);
        }
    }
}