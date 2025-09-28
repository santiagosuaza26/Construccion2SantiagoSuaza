package app.presentation.controller;

import app.application.dto.request.CreateUserRequest;
import app.application.dto.response.CommonResponse;
import app.application.dto.response.UserResponse;
import app.application.service.UserApplicationService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<UserResponse>>> getAllUsers() {
        try {
            CommonResponse<List<UserResponse>> response = userApplicationService.getAllUsers();

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<List<UserResponse>> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener usuarios", "USERS_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{idCard}")
    public ResponseEntity<CommonResponse<UserResponse>> getUserById(
            @PathVariable String idCard) {

        try {
            if (idCard == null || idCard.trim().isEmpty()) {
                CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                    "ID de usuario es requerido", "USER_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            CommonResponse<UserResponse> response = userApplicationService.getUserByIdCard(idCard.trim());

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al buscar usuario", "USER_SEARCH_500");
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
                "Error interno del servidor al crear usuario", "USER_CREATE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{idCard}")
    public ResponseEntity<CommonResponse<UserResponse>> updateUser(
            @PathVariable String idCard,
            @Valid @RequestBody CreateUserRequest updateUserRequest) {

        try {
            if (idCard == null || idCard.trim().isEmpty()) {
                CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                    "ID de usuario es requerido", "USER_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            CommonResponse<UserResponse> response = userApplicationService.updateUser(idCard.trim(), updateUserRequest);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<UserResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al actualizar usuario", "USER_UPDATE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{idCard}")
    public ResponseEntity<CommonResponse<String>> deleteUser(
            @PathVariable String idCard) {

        try {
            if (idCard == null || idCard.trim().isEmpty()) {
                CommonResponse<String> errorResponse = CommonResponse.error(
                    "ID de usuario es requerido", "USER_ID_REQUIRED");
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
                "Error interno del servidor al eliminar usuario", "USER_DELETE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}