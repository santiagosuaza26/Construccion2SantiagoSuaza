package app.infrastructure.adapter.web.controller;

import app.application.dto.request.InventoryItemRequest;
import app.application.dto.response.CommonResponse;
import app.application.dto.response.InventoryResponse;
import app.application.service.InventoryApplicationService;
import app.domain.model.Role;
import app.domain.services.AuthenticationService.AuthenticatedUser;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de inventarios
 *
 * Endpoints para personal de soporte de información:
 * - GET /api/inventory/medications: Listar medicamentos
 * - GET /api/inventory/procedures: Listar procedimientos
 * - GET /api/inventory/diagnostics: Listar ayudas diagnósticas
 * - POST /api/inventory/medications: Agregar medicamento
 * - POST /api/inventory/procedures: Agregar procedimiento
 * - POST /api/inventory/diagnostics: Agregar ayuda diagnóstica
 * - PUT /api/inventory/medications/{id}: Actualizar medicamento
 * - PUT /api/inventory/procedures/{id}: Actualizar procedimiento
 * - PUT /api/inventory/diagnostics/{id}: Actualizar ayuda diagnóstica
 * - DELETE /api/inventory/medications/{id}: Eliminar medicamento
 * - DELETE /api/inventory/procedures/{id}: Eliminar procedimiento
 * - DELETE /api/inventory/diagnostics/{id}: Eliminar ayuda diagnóstica
 * - GET /api/inventory/search: Buscar items por nombre
 * - GET /api/inventory/low-stock: Items con stock bajo
 */
@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class InventoryController {

    private final InventoryApplicationService inventoryApplicationService;

    public InventoryController(InventoryApplicationService inventoryApplicationService) {
        this.inventoryApplicationService = inventoryApplicationService;
    }

    /**
     * Listar todos los medicamentos
     * Solo accesible para personal autorizado
     */
    @GetMapping("/medications")
    public ResponseEntity<CommonResponse<InventoryResponse>> getAllMedications(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            CommonResponse<InventoryResponse> response = inventoryApplicationService.getAllMedications(currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener medicamentos", "MEDICATIONS_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Listar todos los procedimientos
     * Solo accesible para personal autorizado
     */
    @GetMapping("/procedures")
    public ResponseEntity<CommonResponse<InventoryResponse>> getAllProcedures(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            CommonResponse<InventoryResponse> response = inventoryApplicationService.getAllProcedures(currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener procedimientos", "PROCEDURES_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Listar todas las ayudas diagnósticas
     * Solo accesible para personal autorizado
     */
    @GetMapping("/diagnostics")
    public ResponseEntity<CommonResponse<InventoryResponse>> getAllDiagnostics(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            CommonResponse<InventoryResponse> response = inventoryApplicationService.getAllDiagnosticTests(currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener ayudas diagnósticas", "DIAGNOSTICS_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Crear nuevo medicamento
     * Solo accesible para personal de soporte
     */
    @PostMapping("/medications")
    public ResponseEntity<CommonResponse<InventoryResponse>> createMedication(
            @Valid @RequestBody InventoryItemRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            CommonResponse<InventoryResponse> response = inventoryApplicationService.createMedication(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al crear medicamento", "CREATE_MEDICATION_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Crear nuevo procedimiento
     * Solo accesible para personal de soporte
     */
    @PostMapping("/procedures")
    public ResponseEntity<CommonResponse<InventoryResponse>> createProcedure(
            @Valid @RequestBody InventoryItemRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            CommonResponse<InventoryResponse> response = inventoryApplicationService.createProcedure(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al crear procedimiento", "CREATE_PROCEDURE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Crear nueva ayuda diagnóstica
     * Solo accesible para personal de soporte
     */
    @PostMapping("/diagnostics")
    public ResponseEntity<CommonResponse<InventoryResponse>> createDiagnostic(
            @Valid @RequestBody InventoryItemRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            CommonResponse<InventoryResponse> response = inventoryApplicationService.createDiagnosticTest(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al crear ayuda diagnóstica", "CREATE_DIAGNOSTIC_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Actualizar medicamento existente
     * Solo accesible para personal de soporte
     */
    @PutMapping("/medications")
    public ResponseEntity<CommonResponse<InventoryResponse>> updateMedication(
            @Valid @RequestBody InventoryItemRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            CommonResponse<InventoryResponse> response = inventoryApplicationService.updateMedication(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al actualizar medicamento", "UPDATE_MEDICATION_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Actualizar procedimiento existente
     * Solo accesible para personal de soporte
     */
    @PutMapping("/procedures")
    public ResponseEntity<CommonResponse<InventoryResponse>> updateProcedure(
            @Valid @RequestBody InventoryItemRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            CommonResponse<InventoryResponse> response = inventoryApplicationService.updateProcedure(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al actualizar procedimiento", "UPDATE_PROCEDURE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Actualizar ayuda diagnóstica existente
     * Solo accesible para personal de soporte
     */
    @PutMapping("/diagnostics")
    public ResponseEntity<CommonResponse<InventoryResponse>> updateDiagnostic(
            @Valid @RequestBody InventoryItemRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            CommonResponse<InventoryResponse> response = inventoryApplicationService.updateDiagnosticTest(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al actualizar ayuda diagnóstica", "UPDATE_DIAGNOSTIC_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Actualizar stock de medicamento
     * Solo accesible para personal de soporte
     */
    @PutMapping("/medications/stock")
    public ResponseEntity<CommonResponse<String>> updateMedicationStock(
            @RequestParam String medicationId,
            @RequestParam Integer quantity,
            @RequestParam String operation,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (medicationId == null || medicationId.trim().isEmpty()) {
                CommonResponse<String> errorResponse = CommonResponse.error(
                    "ID del medicamento es requerido", "MEDICATION_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            if (quantity == null || quantity <= 0) {
                CommonResponse<String> errorResponse = CommonResponse.error(
                    "Cantidad debe ser positiva", "QUANTITY_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            if (!"ADD".equals(operation) && !"SUBTRACT".equals(operation)) {
                CommonResponse<String> errorResponse = CommonResponse.error(
                    "Operación debe ser ADD o SUBTRACT", "OPERATION_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            CommonResponse<String> response = inventoryApplicationService.updateMedicationStock(
                medicationId.trim(), quantity, operation, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<String> errorResponse = CommonResponse.error(
                "Error interno del servidor al actualizar stock", "UPDATE_STOCK_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Eliminar medicamento
     * Solo accesible para personal de soporte
     */
    @DeleteMapping("/medications/{medicationId}")
    public ResponseEntity<CommonResponse<String>> deleteMedication(
            @PathVariable String medicationId,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            if (medicationId == null || medicationId.trim().isEmpty()) {
                CommonResponse<String> errorResponse = CommonResponse.error(
                    "ID del medicamento es requerido", "MEDICATION_ID_REQUIRED");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            CommonResponse<String> response = inventoryApplicationService.deleteMedication(
                medicationId.trim(), currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<String> errorResponse = CommonResponse.error(
                "Error interno del servidor al eliminar medicamento", "DELETE_MEDICATION_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Método temporal para crear usuario autenticado mock
     * TODO: Reemplazar con implementación real de JWT
     */
    private AuthenticatedUser createMockAuthenticatedUser(String userId, String roleString) {
        if (userId == null) {
            return new AuthenticatedUser("12345678", "Mock User", Role.valueOf(roleString), true);
        }
        return new AuthenticatedUser(userId, "Mock User", Role.valueOf(roleString), true);
    }
}