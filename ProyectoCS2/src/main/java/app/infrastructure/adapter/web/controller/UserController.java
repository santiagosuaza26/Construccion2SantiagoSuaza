package app.infrastructure.adapter.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.application.dto.request.CreateUserRequest;
import app.application.dto.response.CommonResponse;
import app.application.dto.response.UserResponse;
import app.application.service.UserApplicationService;
import jakarta.validation.Valid;

/**
 * Controlador REST para gestión de usuarios del sistema
 *
 * Endpoints:
 * - GET /api/users: Listar usuarios
 * - GET /api/users/{idCard}: Obtener usuario por ID
 * - POST /api/users: Crear nuevo usuario
 * - PUT /api/users/{idCard}: Actualizar usuario
 * - DELETE /api/users/{idCard}: Eliminar usuario
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class UserController {

    // Constantes para mensajes de error
    private static final String USER_ID_REQUIRED_MESSAGE = "ID de usuario es requerido";
    private static final String USER_ID_REQUIRED_CODE = "USER_ID_REQUIRED";
    private static final String USERS_ERROR_MESSAGE = "Error interno del servidor al obtener usuarios";
    private static final String USER_SEARCH_ERROR_MESSAGE = "Error interno del servidor al buscar usuario";
    private static final String USER_CREATE_ERROR_MESSAGE = "Error interno del servidor al crear usuario";
    private static final String USER_UPDATE_ERROR_MESSAGE = "Error interno del servidor al actualizar usuario";
    private static final String USER_DELETE_ERROR_MESSAGE = "Error interno del servidor al eliminar usuario";

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    /**
     * Valida que el ID del usuario no sea nulo o vacío
     * @param idCard ID del usuario a validar
     * @return true si el ID es válido, false si es inválido
     */
    private boolean isUserIdValid(String idCard) {
        return idCard != null && !idCard.trim().isEmpty();
    }

    /**
     * Crea una respuesta de error para ID de usuario requerido
     * @return ResponseEntity con error de ID requerido
     */
    private ResponseEntity<CommonResponse<UserResponse>> createUserIdRequiredErrorResponse() {
        CommonResponse<UserResponse> errorResponse = CommonResponse.error(
            USER_ID_REQUIRED_MESSAGE, USER_ID_REQUIRED_CODE);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<UserResponse>>> getAllUsers() {
        try {
            CommonResponse<List<UserResponse>> response = userApplicationService.getAllUsers();

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            CommonResponse<List<UserResponse>> errorResponse = CommonResponse.error(
                USERS_ERROR_MESSAGE, "USERS_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{idCard}")
    public ResponseEntity<CommonResponse<UserResponse>> getUserById(
            @PathVariable String idCard) {

        try {
            // Validar ID del usuario
            if (!isUserIdValid(idCard)) {
                return createUserIdRequiredErrorResponse();
            }

            CommonResponse<UserResponse> response = userApplicationService.getUserByIdCard(idCard.trim());

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                USER_SEARCH_ERROR_MESSAGE, "USER_SEARCH_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<CommonResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequest) {

        try {
            CommonResponse<UserResponse> response = userApplicationService.createUser(createUserRequest);

            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                USER_CREATE_ERROR_MESSAGE, "USER_CREATE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{idCard}")
    public ResponseEntity<CommonResponse<UserResponse>> updateUser(
            @PathVariable String idCard,
            @Valid @RequestBody CreateUserRequest updateUserRequest) {

        try {
            // Validar ID del usuario
            if (!isUserIdValid(idCard)) {
                return createUserIdRequiredErrorResponse();
            }

            CommonResponse<UserResponse> response = userApplicationService.updateUser(idCard.trim(), updateUserRequest);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                USER_UPDATE_ERROR_MESSAGE, "USER_UPDATE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{idCard}")
    public ResponseEntity<CommonResponse<String>> deleteUser(
            @PathVariable String idCard) {

        try {
            // Validar ID del usuario
            if (!isUserIdValid(idCard)) {
                CommonResponse<String> errorResponse = CommonResponse.error(
                    USER_ID_REQUIRED_MESSAGE, USER_ID_REQUIRED_CODE);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            CommonResponse<String> response = userApplicationService.deleteUser(idCard.trim());

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<String> errorResponse = CommonResponse.error(
                USER_DELETE_ERROR_MESSAGE, "USER_DELETE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}