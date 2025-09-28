package app.presentation.controller;

import app.application.dto.request.LoginRequest;
import app.application.dto.response.AuthResponse;
import app.application.dto.response.CommonResponse;
import app.application.service.AuthApplicationService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para manejo de autenticación
 *
 * Endpoints:
 * - POST /api/auth/login: Autenticar usuarios
 * - POST /api/auth/logout: Cerrar sesión
 * - GET /api/auth/me: Obtener información del usuario actual
 * - GET /api/auth/permissions: Verificar permisos
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class AuthController {

    private final AuthApplicationService authApplicationService;

    public AuthController(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
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
            CommonResponse<AuthResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor durante autenticación", "AUTH_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<String>> logout(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // Si no hay header, intentar obtener de token JWT (futuro)
            String actualUserId = userId != null ? userId : "unknown";

            CommonResponse<String> response = authApplicationService.logout(actualUserId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            CommonResponse<String> errorResponse = CommonResponse.error(
                "Error interno del servidor durante logout", "LOGOUT_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<AuthResponse>> getCurrentUser(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // Si no hay header, intentar obtener de token JWT (futuro)
            if (userId == null) {
                CommonResponse<AuthResponse> errorResponse = CommonResponse.error(
                    "Usuario no autenticado", "AUTH_401");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            CommonResponse<AuthResponse> response = authApplicationService.getCurrentUserInfo(userId);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            CommonResponse<AuthResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor", "ME_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/permissions")
    public ResponseEntity<CommonResponse<Boolean>> checkPermission(
            @RequestHeader(value = "User-ID", required = false) String userId,
            @RequestParam String permission) {

        try {
            if (userId == null) {
                CommonResponse<Boolean> errorResponse = CommonResponse.error(
                    "Usuario no autenticado", "AUTH_401");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            CommonResponse<Boolean> response = authApplicationService.hasPermission(userId, permission);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            CommonResponse<Boolean> errorResponse = CommonResponse.error(
                "Error verificando permisos", "PERM_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/access")
    public ResponseEntity<CommonResponse<Boolean>> checkResourceAccess(
            @RequestHeader(value = "User-ID", required = false) String userId,
            @RequestParam String resourceType,
            @RequestParam String action) {

        try {
            if (userId == null) {
                CommonResponse<Boolean> errorResponse = CommonResponse.error(
                    "Usuario no autenticado", "AUTH_401");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            CommonResponse<Boolean> response = authApplicationService.canAccessResource(
                userId, resourceType, action);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            CommonResponse<Boolean> errorResponse = CommonResponse.error(
                "Error verificando acceso a recurso", "ACCESS_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}