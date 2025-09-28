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
 * Controlador REST para funcionalidades de Soporte de Información
 *
 * Endpoints exclusivos para Soporte de Información:
 * - GET /api/support/inventory/medications: Listar medicamentos
 * - GET /api/support/inventory/procedures: Listar procedimientos
 * - GET /api/support/inventory/diagnostics: Listar ayudas diagnósticas
 * - POST /api/support/inventory/medications: Crear medicamento
 * - POST /api/support/inventory/procedures: Crear procedimiento
 * - POST /api/support/inventory/diagnostics: Crear ayuda diagnóstica
 * - PUT /api/support/inventory/medications: Actualizar medicamento
 * - PUT /api/support/inventory/procedures: Actualizar procedimiento
 * - PUT /api/support/inventory/diagnostics: Actualizar ayuda diagnóstica
 * - DELETE /api/support/inventory/medications/{id}: Eliminar medicamento
 * - GET /api/support/inventory/search: Buscar items
 * - GET /api/support/inventory/low-stock: Items con stock bajo
 * - PUT /api/support/inventory/stock: Actualizar stock
 *
 * RESTRICCIONES:
 * - NO puede acceder a información de pacientes
 * - NO puede generar facturas
 * - NO puede crear órdenes médicas
 * - NO puede acceder a historia clínica
 */
@RestController
@RequestMapping("/api/support")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class SupportController {

    private final InventoryApplicationService inventoryApplicationService;

    public SupportController(InventoryApplicationService inventoryApplicationService) {
        this.inventoryApplicationService = inventoryApplicationService;
    }

    /**
     * Listar todos los medicamentos
     * Exclusivo para Soporte de Información
     */
    @GetMapping("/inventory/medications")
    public ResponseEntity<CommonResponse<InventoryResponse>> getAllMedications(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            if (!currentUser.canManageInventory()) {
                CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Soporte de Información puede gestionar inventarios", "SUPPORT_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<InventoryResponse> response = inventoryApplicationService.getAllMedications(currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener medicamentos", "SUPPORT_MEDICATIONS_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Listar todos los procedimientos
     * Exclusivo para Soporte de Información
     */
    @GetMapping("/inventory/procedures")
    public ResponseEntity<CommonResponse<InventoryResponse>> getAllProcedures(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            if (!currentUser.canManageInventory()) {
                CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Soporte de Información puede gestionar inventarios", "SUPPORT_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<InventoryResponse> response = inventoryApplicationService.getAllProcedures(currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener procedimientos", "SUPPORT_PROCEDURES_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Listar todas las ayudas diagnósticas
     * Exclusivo para Soporte de Información
     */
    @GetMapping("/inventory/diagnostics")
    public ResponseEntity<CommonResponse<InventoryResponse>> getAllDiagnostics(
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            if (!currentUser.canManageInventory()) {
                CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Soporte de Información puede gestionar inventarios", "SUPPORT_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<InventoryResponse> response = inventoryApplicationService.getAllDiagnosticTests(currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al obtener ayudas diagnósticas", "SUPPORT_DIAGNOSTICS_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Crear nuevo medicamento
     * Exclusivo para Soporte de Información
     */
    @PostMapping("/inventory/medications")
    public ResponseEntity<CommonResponse<InventoryResponse>> createMedication(
            @Valid @RequestBody InventoryItemRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            if (!currentUser.canManageInventory()) {
                CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Soporte de Información puede gestionar inventarios", "SUPPORT_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<InventoryResponse> response = inventoryApplicationService.createMedication(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al crear medicamento", "SUPPORT_CREATE_MEDICATION_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Crear nuevo procedimiento
     * Exclusivo para Soporte de Información
     */
    @PostMapping("/inventory/procedures")
    public ResponseEntity<CommonResponse<InventoryResponse>> createProcedure(
            @Valid @RequestBody InventoryItemRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            if (!currentUser.canManageInventory()) {
                CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Soporte de Información puede gestionar inventarios", "SUPPORT_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<InventoryResponse> response = inventoryApplicationService.createProcedure(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al crear procedimiento", "SUPPORT_CREATE_PROCEDURE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Crear nueva ayuda diagnóstica
     * Exclusivo para Soporte de Información
     */
    @PostMapping("/inventory/diagnostics")
    public ResponseEntity<CommonResponse<InventoryResponse>> createDiagnostic(
            @Valid @RequestBody InventoryItemRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            if (!currentUser.canManageInventory()) {
                CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Soporte de Información puede gestionar inventarios", "SUPPORT_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<InventoryResponse> response = inventoryApplicationService.createDiagnosticTest(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al crear ayuda diagnóstica", "SUPPORT_CREATE_DIAGNOSTIC_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Actualizar medicamento existente
     * Exclusivo para Soporte de Información
     */
    @PutMapping("/inventory/medications")
    public ResponseEntity<CommonResponse<InventoryResponse>> updateMedication(
            @Valid @RequestBody InventoryItemRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            if (!currentUser.canManageInventory()) {
                CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Soporte de Información puede gestionar inventarios", "SUPPORT_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<InventoryResponse> response = inventoryApplicationService.updateMedication(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al actualizar medicamento", "SUPPORT_UPDATE_MEDICATION_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Actualizar procedimiento existente
     * Exclusivo para Soporte de Información
     */
    @PutMapping("/inventory/procedures")
    public ResponseEntity<CommonResponse<InventoryResponse>> updateProcedure(
            @Valid @RequestBody InventoryItemRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            if (!currentUser.canManageInventory()) {
                CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Soporte de Información puede gestionar inventarios", "SUPPORT_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<InventoryResponse> response = inventoryApplicationService.updateProcedure(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al actualizar procedimiento", "SUPPORT_UPDATE_PROCEDURE_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Actualizar ayuda diagnóstica existente
     * Exclusivo para Soporte de Información
     */
    @PutMapping("/inventory/diagnostics")
    public ResponseEntity<CommonResponse<InventoryResponse>> updateDiagnostic(
            @Valid @RequestBody InventoryItemRequest request,
            @RequestHeader(value = "User-ID", required = false) String userId) {

        try {
            // TODO: Implementar autenticación real con JWT
            AuthenticatedUser currentUser = createMockAuthenticatedUser(userId, "SUPPORT");

            if (!currentUser.canManageInventory()) {
                CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Soporte de Información puede gestionar inventarios", "SUPPORT_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<InventoryResponse> response = inventoryApplicationService.updateDiagnosticTest(request, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<InventoryResponse> errorResponse = CommonResponse.error(
                "Error interno del servidor al actualizar ayuda diagnóstica", "SUPPORT_UPDATE_DIAGNOSTIC_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Eliminar medicamento
     * Exclusivo para Soporte de Información
     */
    @DeleteMapping("/inventory/medications/{medicationId}")
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

            if (!currentUser.canManageInventory()) {
                CommonResponse<String> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Soporte de Información puede gestionar inventarios", "SUPPORT_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<String> response = inventoryApplicationService.deleteMedication(
                medicationId.trim(), currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<String> errorResponse = CommonResponse.error(
                "Error interno del servidor al eliminar medicamento", "SUPPORT_DELETE_MEDICATION_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Actualizar stock de medicamento
     * Exclusivo para Soporte de Información
     */
    @PutMapping("/inventory/stock")
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

            if (!currentUser.canManageInventory()) {
                CommonResponse<String> errorResponse = CommonResponse.error(
                    "Acceso denegado - Solo Soporte de Información puede gestionar inventarios", "SUPPORT_ACCESS_DENIED");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            CommonResponse<String> response = inventoryApplicationService.updateMedicationStock(
                medicationId.trim(), quantity, operation, currentUser);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            CommonResponse<String> errorResponse = CommonResponse.error(
                "Error interno del servidor al actualizar stock", "SUPPORT_UPDATE_STOCK_500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Método temporal para crear usuario autenticado mock
     * TODO: Reemplazar con implementación real de JWT
     */
    private AuthenticatedUser createMockAuthenticatedUser(String userId, String roleString) {
        if (userId == null) {
            return new AuthenticatedUser("12345678", "Mock Support User", Role.valueOf(roleString), true);
        }
        return new AuthenticatedUser(userId, "Mock Support User", Role.valueOf(roleString), true);
    }
}