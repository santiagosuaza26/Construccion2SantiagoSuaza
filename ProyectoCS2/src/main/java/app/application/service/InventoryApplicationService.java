package app.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import app.application.dto.request.InventoryItemRequest;
import app.application.dto.response.CommonResponse;
import app.application.dto.response.InventoryResponse;
import app.application.mapper.InventoryMapper;
import app.domain.model.DiagnosticTest;
import app.domain.model.Medication;
import app.domain.model.ProcedureType;
import app.domain.model.Role;
import app.domain.model.Specialty;
import app.domain.services.AuthenticationService.AuthenticatedUser;
import app.domain.services.InventoryService;

@Service
public class InventoryApplicationService {
    
    private final InventoryService inventoryService;
    private final InventoryMapper inventoryMapper;
    
    public InventoryApplicationService(InventoryService inventoryService,InventoryMapper inventoryMapper) {
        this.inventoryService = inventoryService;
        this.inventoryMapper = inventoryMapper;
    }
    
    public CommonResponse<InventoryResponse> createMedication(InventoryItemRequest request, AuthenticatedUser currentUser) {
        try {
            if (!canManageInventory(currentUser)) {
                logUnauthorizedAccess(currentUser, "CREATE_MEDICATION");
                return CommonResponse.error("Access denied - Only Support staff can manage inventory", "INV_001");
            }
            
            validateMedicationRequest(request);
            
            Medication medication = inventoryMapper.toMedication(request);
            Medication createdMedication = inventoryService.createMedication(medication);
            InventoryResponse response = inventoryMapper.toInventoryResponse(createdMedication);
            
            logMedicationCreated(createdMedication, currentUser);
            
            return CommonResponse.success("Medication created successfully", response);
            
        } catch (IllegalArgumentException e) {
            logValidationError("createMedication", e, currentUser);
            return CommonResponse.error(e.getMessage(), "INV_002");
        } catch (Exception e) {
            logSystemError("createMedication", e, currentUser);
            return CommonResponse.error("Internal error creating medication", "INV_003");
        }
    }
    
    public CommonResponse<InventoryResponse> updateMedication(InventoryItemRequest request, AuthenticatedUser currentUser) {
        try {
            if (!canManageInventory(currentUser)) {
                logUnauthorizedAccess(currentUser, "UPDATE_MEDICATION");
                return CommonResponse.error("Access denied - Only Support staff can manage inventory", "INV_004");
            }
            
            validateMedicationRequest(request);
            
            Medication medication = inventoryMapper.toMedication(request);
            Medication updatedMedication = inventoryService.updateMedication(medication);
            InventoryResponse response = inventoryMapper.toInventoryResponse(updatedMedication);
            
            logMedicationUpdated(updatedMedication, currentUser);
            
            return CommonResponse.success("Medication updated successfully", response);
            
        } catch (IllegalArgumentException e) {
            logValidationError("updateMedication", e, currentUser);
            return CommonResponse.error(e.getMessage(), "INV_005");
        } catch (Exception e) {
            logSystemError("updateMedication", e, currentUser);
            return CommonResponse.error("Internal error updating medication", "INV_006");
        }
    }
    
    public CommonResponse<String> deleteMedication(String medicationId, AuthenticatedUser currentUser) {
        try {
            if (!canManageInventory(currentUser)) {
                logUnauthorizedAccess(currentUser, "DELETE_MEDICATION");
                return CommonResponse.error("Access denied - Only Support staff can manage inventory", "INV_007");
            }
            
            if (medicationId == null || medicationId.isBlank()) {
                return CommonResponse.error("Medication ID is required", "INV_008");
            }
            
            inventoryService.deleteMedication(medicationId);
            
            logMedicationDeleted(medicationId, currentUser);
            
            return CommonResponse.success("Medication deleted successfully: " + medicationId);
            
        } catch (IllegalArgumentException e) {
            return CommonResponse.error(e.getMessage(), "INV_009");
        } catch (Exception e) {
            logSystemError("deleteMedication", e, currentUser);
            return CommonResponse.error("Internal error deleting medication", "INV_010");
        }
    }
    
    public CommonResponse<InventoryResponse> getAllMedications(AuthenticatedUser currentUser) {
        try {
            if (!canViewMedications(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_MEDICATIONS");
                return CommonResponse.error("Access denied - Cannot view medications", "INV_011");
            }
            
            List<Medication> medications = inventoryService.getAllMedications();
            InventoryResponse response = inventoryMapper.toMedicationListResponse(medications);
            
            logMedicationsListed(medications.size(), currentUser);
            
            return CommonResponse.success("Medications retrieved successfully", response);
            
        } catch (Exception e) {
            logSystemError("getAllMedications", e, currentUser);
            return CommonResponse.error("Internal error retrieving medications", "INV_012");
        }
    }
    
    public CommonResponse<String> updateMedicationStock(String medicationId, Integer quantity, 
                                                        String operation, AuthenticatedUser currentUser) {
        try {
            if (!canManageInventory(currentUser)) {
                logUnauthorizedAccess(currentUser, "UPDATE_STOCK");
                return CommonResponse.error("Access denied - Only Support staff can update stock", "INV_013");
            }
            
            if (medicationId == null || medicationId.isBlank()) {
                return CommonResponse.error("Medication ID is required", "INV_014");
            }
            
            if (quantity == null || quantity <= 0) {
                return CommonResponse.error("Quantity must be positive", "INV_015");
            }
            
            if (!"ADD".equals(operation) && !"SUBTRACT".equals(operation)) {
                return CommonResponse.error("Operation must be ADD or SUBTRACT", "INV_016");
            }
            
            boolean isAddition = "ADD".equals(operation);
            inventoryService.updateMedicationStock(medicationId, quantity, isAddition);
            
            logStockUpdated(medicationId, quantity, operation, currentUser);
            
            return CommonResponse.success(String.format("Stock updated: %s %d units for medication %s", 
                operation, quantity, medicationId));
            
        } catch (IllegalArgumentException e) {
            return CommonResponse.error(e.getMessage(), "INV_017");
        } catch (Exception e) {
            logSystemError("updateMedicationStock", e, currentUser);
            return CommonResponse.error("Internal error updating stock", "INV_018");
        }
    }
    
    public CommonResponse<InventoryResponse> createProcedure(InventoryItemRequest request, AuthenticatedUser currentUser) {
        try {
            if (!canManageInventory(currentUser)) {
                logUnauthorizedAccess(currentUser, "CREATE_PROCEDURE");
                return CommonResponse.error("Access denied - Only Support staff can manage inventory", "INV_019");
            }
            
            validateProcedureRequest(request);
            
            ProcedureType procedure = inventoryMapper.toProcedureType(request);
            ProcedureType createdProcedure = inventoryService.createProcedure(procedure);
            InventoryResponse response = inventoryMapper.toInventoryResponse(createdProcedure);
            
            logProcedureCreated(createdProcedure, currentUser);
            
            return CommonResponse.success("Procedure created successfully", response);
            
        } catch (IllegalArgumentException e) {
            logValidationError("createProcedure", e, currentUser);
            return CommonResponse.error(e.getMessage(), "INV_020");
        } catch (Exception e) {
            logSystemError("createProcedure", e, currentUser);
            return CommonResponse.error("Internal error creating procedure", "INV_021");
        }
    }
    
    public CommonResponse<InventoryResponse> updateProcedure(InventoryItemRequest request, AuthenticatedUser currentUser) {
        try {
            if (!canManageInventory(currentUser)) {
                logUnauthorizedAccess(currentUser, "UPDATE_PROCEDURE");
                return CommonResponse.error("Access denied - Only Support staff can manage inventory", "INV_022");
            }
            
            validateProcedureRequest(request);
            
            ProcedureType procedure = inventoryMapper.toProcedureType(request);
            ProcedureType updatedProcedure = inventoryService.updateProcedure(procedure);
            InventoryResponse response = inventoryMapper.toInventoryResponse(updatedProcedure);
            
            logProcedureUpdated(updatedProcedure, currentUser);
            
            return CommonResponse.success("Procedure updated successfully", response);
            
        } catch (IllegalArgumentException e) {
            logValidationError("updateProcedure", e, currentUser);
            return CommonResponse.error(e.getMessage(), "INV_023");
        } catch (Exception e) {
            logSystemError("updateProcedure", e, currentUser);
            return CommonResponse.error("Internal error updating procedure", "INV_024");
        }
    }
    
    public CommonResponse<String> deleteProcedure(String procedureId, AuthenticatedUser currentUser) {
        try {
            if (!canManageInventory(currentUser)) {
                logUnauthorizedAccess(currentUser, "DELETE_PROCEDURE");
                return CommonResponse.error("Access denied - Only Support staff can manage inventory", "INV_025");
            }
            
            if (procedureId == null || procedureId.isBlank()) {
                return CommonResponse.error("Procedure ID is required", "INV_026");
            }
            
            inventoryService.deleteProcedure(procedureId);
            
            logProcedureDeleted(procedureId, currentUser);
            
            return CommonResponse.success("Procedure deleted successfully: " + procedureId);
            
        } catch (IllegalArgumentException e) {
            return CommonResponse.error(e.getMessage(), "INV_027");
        } catch (Exception e) {
            logSystemError("deleteProcedure", e, currentUser);
            return CommonResponse.error("Internal error deleting procedure", "INV_028");
        }
    }
    
    public CommonResponse<InventoryResponse> getAllProcedures(AuthenticatedUser currentUser) {
        try {
            if (!canViewProcedures(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_PROCEDURES");
                return CommonResponse.error("Access denied - Cannot view procedures", "INV_029");
            }
            
            List<ProcedureType> procedures = inventoryService.getAllProcedures();
            InventoryResponse response = inventoryMapper.toProcedureListResponse(procedures);
            
            logProceduresListed(procedures.size(), currentUser);
            
            return CommonResponse.success("Procedures retrieved successfully", response);
            
        } catch (Exception e) {
            logSystemError("getAllProcedures", e, currentUser);
            return CommonResponse.error("Internal error retrieving procedures", "INV_030");
        }
    }
    
    public CommonResponse<InventoryResponse> createDiagnosticTest(InventoryItemRequest request, AuthenticatedUser currentUser) {
        try {
            if (!canManageInventory(currentUser)) {
                logUnauthorizedAccess(currentUser, "CREATE_DIAGNOSTIC");
                return CommonResponse.error("Access denied - Only Support staff can manage inventory", "INV_031");
            }
            
            validateDiagnosticRequest(request);
            
            DiagnosticTest diagnostic = inventoryMapper.toDiagnosticTest(request);
            DiagnosticTest createdDiagnostic = inventoryService.createDiagnosticTest(diagnostic);
            InventoryResponse response = inventoryMapper.toInventoryResponse(createdDiagnostic);
            
            logDiagnosticCreated(createdDiagnostic, currentUser);
            
            return CommonResponse.success("Diagnostic test created successfully", response);
            
        } catch (IllegalArgumentException e) {
            logValidationError("createDiagnosticTest", e, currentUser);
            return CommonResponse.error(e.getMessage(), "INV_032");
        } catch (Exception e) {
            logSystemError("createDiagnosticTest", e, currentUser);
            return CommonResponse.error("Internal error creating diagnostic test", "INV_033");
        }
    }
    
    public CommonResponse<InventoryResponse> updateDiagnosticTest(InventoryItemRequest request, AuthenticatedUser currentUser) {
        try {
            if (!canManageInventory(currentUser)) {
                logUnauthorizedAccess(currentUser, "UPDATE_DIAGNOSTIC");
                return CommonResponse.error("Access denied - Only Support staff can manage inventory", "INV_034");
            }
            
            validateDiagnosticRequest(request);
            
            DiagnosticTest diagnostic = inventoryMapper.toDiagnosticTest(request);
            DiagnosticTest updatedDiagnostic = inventoryService.updateDiagnosticTest(diagnostic);
            InventoryResponse response = inventoryMapper.toInventoryResponse(updatedDiagnostic);
            
            logDiagnosticUpdated(updatedDiagnostic, currentUser);
            
            return CommonResponse.success("Diagnostic test updated successfully", response);
            
        } catch (IllegalArgumentException e) {
            logValidationError("updateDiagnosticTest", e, currentUser);
            return CommonResponse.error(e.getMessage(), "INV_035");
        } catch (Exception e) {
            logSystemError("updateDiagnosticTest", e, currentUser);
            return CommonResponse.error("Internal error updating diagnostic test", "INV_036");
        }
    }
    
    public CommonResponse<String> deleteDiagnosticTest(String diagnosticId, AuthenticatedUser currentUser) {
        try {
            if (!canManageInventory(currentUser)) {
                logUnauthorizedAccess(currentUser, "DELETE_DIAGNOSTIC");
                return CommonResponse.error("Access denied - Only Support staff can manage inventory", "INV_037");
            }
            
            if (diagnosticId == null || diagnosticId.isBlank()) {
                return CommonResponse.error("Diagnostic test ID is required", "INV_038");
            }
            
            inventoryService.deleteDiagnosticTest(diagnosticId);
            
            logDiagnosticDeleted(diagnosticId, currentUser);
            
            return CommonResponse.success("Diagnostic test deleted successfully: " + diagnosticId);
            
        } catch (IllegalArgumentException e) {
            return CommonResponse.error(e.getMessage(), "INV_039");
        } catch (Exception e) {
            logSystemError("deleteDiagnosticTest", e, currentUser);
            return CommonResponse.error("Internal error deleting diagnostic test", "INV_040");
        }
    }
    
    public CommonResponse<InventoryResponse> getAllDiagnosticTests(AuthenticatedUser currentUser) {
        try {
            if (!canViewDiagnostics(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_DIAGNOSTICS");
                return CommonResponse.error("Access denied - Cannot view diagnostic tests", "INV_041");
            }
            
            List<DiagnosticTest> diagnostics = inventoryService.getAllDiagnosticTests();
            InventoryResponse response = inventoryMapper.toDiagnosticListResponse(diagnostics);
            
            logDiagnosticsListed(diagnostics.size(), currentUser);
            
            return CommonResponse.success("Diagnostic tests retrieved successfully", response);
            
        } catch (Exception e) {
            logSystemError("getAllDiagnosticTests", e, currentUser);
            return CommonResponse.error("Internal error retrieving diagnostic tests", "INV_042");
        }
    }
    
    public CommonResponse<InventoryResponse> createSpecialty(InventoryItemRequest request, AuthenticatedUser currentUser) {
        try {
            if (!canManageInventory(currentUser)) {
                logUnauthorizedAccess(currentUser, "CREATE_SPECIALTY");
                return CommonResponse.error("Access denied - Only Support staff can manage inventory", "INV_043");
            }
            
            validateSpecialtyRequest(request);
            
            Specialty specialty = inventoryMapper.toSpecialty(request);
            Specialty createdSpecialty = inventoryService.createSpecialty(specialty);
            InventoryResponse response = inventoryMapper.toInventoryResponse(createdSpecialty);
            
            logSpecialtyCreated(createdSpecialty, currentUser);
            
            return CommonResponse.success("Specialty created successfully", response);
            
        } catch (IllegalArgumentException e) {
            logValidationError("createSpecialty", e, currentUser);
            return CommonResponse.error(e.getMessage(), "INV_044");
        } catch (Exception e) {
            logSystemError("createSpecialty", e, currentUser);
            return CommonResponse.error("Internal error creating specialty", "INV_045");
        }
    }
    
    public CommonResponse<InventoryResponse> getAllSpecialties(AuthenticatedUser currentUser) {
        try {
            if (!canViewSpecialties(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_SPECIALTIES");
                return CommonResponse.error("Access denied - Cannot view specialties", "INV_046");
            }
            
            List<Specialty> specialties = inventoryService.getAllSpecialties();
            InventoryResponse response = inventoryMapper.toSpecialtyListResponse(specialties);
            
            logSpecialtiesListed(specialties.size(), currentUser);
            
            return CommonResponse.success("Specialties retrieved successfully", response);
            
        } catch (Exception e) {
            logSystemError("getAllSpecialties", e, currentUser);
            return CommonResponse.error("Internal error retrieving specialties", "INV_047");
        }
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE VALIDACIÓN
    // =============================================================================
    
    private void validateMedicationRequest(InventoryItemRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Medication request cannot be null");
        }
        
        if (!request.isMedication()) {
            throw new IllegalArgumentException("Request must be for MEDICATION type");
        }
    }
    
    private void validateProcedureRequest(InventoryItemRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Procedure request cannot be null");
        }
        
        if (!request.isProcedure()) {
            throw new IllegalArgumentException("Request must be for PROCEDURE type");
        }
    }
    
    private void validateDiagnosticRequest(InventoryItemRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Diagnostic request cannot be null");
        }
        
        if (!request.isDiagnostic()) {
            throw new IllegalArgumentException("Request must be for DIAGNOSTIC type");
        }
    }
    
    private void validateSpecialtyRequest(InventoryItemRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Specialty request cannot be null");
        }
        
        if (!request.isSpecialty()) {
            throw new IllegalArgumentException("Request must be for SPECIALTY type");
        }
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE AUTORIZACIÓN
    // =============================================================================
    
    private boolean canManageInventory(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.SUPPORT && user.canManageInventory();
    }
    
    private boolean canViewMedications(AuthenticatedUser user) {
        return user != null && user.canAccessMedicationData();
    }
    
    private boolean canViewProcedures(AuthenticatedUser user) {
        return user != null && user.canAccessProcedureData();
    }
    
    private boolean canViewDiagnostics(AuthenticatedUser user) {
        return user != null && user.canAccessProcedureData();
    }
    
    private boolean canViewSpecialties(AuthenticatedUser user) {
        return user != null && (user.getRole() == Role.DOCTOR || user.getRole() == Role.NURSE || 
                                user.getRole() == Role.SUPPORT);
    }
    
    // =============================================================================
    // MÉTODOS DE LOGGING Y AUDITORÍA
    // =============================================================================
    
    private void logMedicationCreated(Medication medication, AuthenticatedUser currentUser) {
        System.out.printf("MEDICATION CREATED: %s created medication %s (%s) at %s%n",
            currentUser.getFullName(), medication.getName(), medication.getMedicationId(),
            java.time.LocalDateTime.now());
    }
    
    private void logMedicationUpdated(Medication medication, AuthenticatedUser currentUser) {
        System.out.printf("MEDICATION UPDATED: %s updated medication %s (%s) at %s%n",
            currentUser.getFullName(), medication.getName(), medication.getMedicationId(),
            java.time.LocalDateTime.now());
    }
    
    private void logMedicationDeleted(String medicationId, AuthenticatedUser currentUser) {
        System.out.printf("MEDICATION DELETED: %s deleted medication %s at %s%n",
            currentUser.getFullName(), medicationId, java.time.LocalDateTime.now());
    }
    
    private void logMedicationsListed(int count, AuthenticatedUser currentUser) {
        System.out.printf("MEDICATIONS LISTED: %s retrieved %d medications at %s%n",
            currentUser.getFullName(), count, java.time.LocalDateTime.now());
    }
    
    private void logStockUpdated(String medicationId, Integer quantity, String operation, AuthenticatedUser currentUser) {
        System.out.printf("STOCK UPDATED: %s performed %s operation of %d units for medication %s at %s%n",
            currentUser.getFullName(), operation, quantity, medicationId, java.time.LocalDateTime.now());
    }
    
    private void logProcedureCreated(ProcedureType procedure, AuthenticatedUser currentUser) {
        System.out.printf("PROCEDURE CREATED: %s created procedure %s (%s) at %s%n",
            currentUser.getFullName(), procedure.getName(), procedure.getProcedureId(),
            java.time.LocalDateTime.now());
    }
    
    private void logProcedureUpdated(ProcedureType procedure, AuthenticatedUser currentUser) {
        System.out.printf("PROCEDURE UPDATED: %s updated procedure %s (%s) at %s%n",
            currentUser.getFullName(), procedure.getName(), procedure.getProcedureId(),
            java.time.LocalDateTime.now());
    }
    
    private void logProcedureDeleted(String procedureId, AuthenticatedUser currentUser) {
        System.out.printf("PROCEDURE DELETED: %s deleted procedure %s at %s%n",
            currentUser.getFullName(), procedureId, java.time.LocalDateTime.now());
    }
    
    private void logProceduresListed(int count, AuthenticatedUser currentUser) {
        System.out.printf("PROCEDURES LISTED: %s retrieved %d procedures at %s%n",
            currentUser.getFullName(), count, java.time.LocalDateTime.now());
    }
    
    private void logDiagnosticCreated(DiagnosticTest diagnostic, AuthenticatedUser currentUser) {
        System.out.printf("DIAGNOSTIC CREATED: %s created diagnostic %s (%s) at %s%n",
            currentUser.getFullName(), diagnostic.getName(), diagnostic.getDiagnosticId(),
            java.time.LocalDateTime.now());
    }
    
    private void logDiagnosticUpdated(DiagnosticTest diagnostic, AuthenticatedUser currentUser) {
        System.out.printf("DIAGNOSTIC UPDATED: %s updated diagnostic %s (%s) at %s%n",
            currentUser.getFullName(), diagnostic.getName(), diagnostic.getDiagnosticId(),
            java.time.LocalDateTime.now());
    }
    
    private void logDiagnosticDeleted(String diagnosticId, AuthenticatedUser currentUser) {
        System.out.printf("DIAGNOSTIC DELETED: %s deleted diagnostic %s at %s%n",
            currentUser.getFullName(), diagnosticId, java.time.LocalDateTime.now());
    }
    
    private void logDiagnosticsListed(int count, AuthenticatedUser currentUser) {
        System.out.printf("DIAGNOSTICS LISTED: %s retrieved %d diagnostics at %s%n",
            currentUser.getFullName(), count, java.time.LocalDateTime.now());
    }
    
    private void logSpecialtyCreated(Specialty specialty, AuthenticatedUser currentUser) {
        System.out.printf("SPECIALTY CREATED: %s created specialty %s (%s) at %s%n",
            currentUser.getFullName(), specialty.getName(), specialty.getSpecialtyId(),
            java.time.LocalDateTime.now());
    }
    
    private void logSpecialtiesListed(int count, AuthenticatedUser currentUser) {
        System.out.printf("SPECIALTIES LISTED: %s retrieved %d specialties at %s%n",
            currentUser.getFullName(), count, java.time.LocalDateTime.now());
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

    // =============================================================================
    // MÉTODOS ADICIONALES PARA EL CONTROLADOR
    // =============================================================================

    public CommonResponse<InventoryResponse> addMedication(InventoryItemRequest request, AuthenticatedUser currentUser) {
        return createMedication(request, currentUser);
    }

    public CommonResponse<InventoryResponse> addProcedure(InventoryItemRequest request, AuthenticatedUser currentUser) {
        return createProcedure(request, currentUser);
    }

    public CommonResponse<InventoryResponse> addDiagnostic(InventoryItemRequest request, AuthenticatedUser currentUser) {
        return createDiagnosticTest(request, currentUser);
    }

    public CommonResponse<InventoryResponse> updateMedication(String id, InventoryItemRequest request, AuthenticatedUser currentUser) {
        // Agregar ID al request para actualización
        InventoryItemRequest updateRequest = new InventoryItemRequest(
            request.getName(), request.getDescription(), request.getCost(),
            request.getType(), request.getStock(), id
        );
        return updateMedication(updateRequest, currentUser);
    }

    public CommonResponse<InventoryResponse> updateProcedure(String id, InventoryItemRequest request, AuthenticatedUser currentUser) {
        // Agregar ID al request para actualización
        InventoryItemRequest updateRequest = new InventoryItemRequest(
            request.getName(), request.getDescription(), request.getCost(),
            request.getType(), request.getStock(), id
        );
        return updateProcedure(updateRequest, currentUser);
    }

    public CommonResponse<InventoryResponse> updateDiagnostic(String id, InventoryItemRequest request, AuthenticatedUser currentUser) {
        // Agregar ID al request para actualización
        InventoryItemRequest updateRequest = new InventoryItemRequest(
            request.getName(), request.getDescription(), request.getCost(),
            request.getType(), request.getStock(), id
        );
        return updateDiagnosticTest(updateRequest, currentUser);
    }

    public CommonResponse<List<InventoryResponse>> searchInventory(String query, String type, AuthenticatedUser currentUser) {
        try {
            if (!canManageInventory(currentUser)) {
                logUnauthorizedAccess(currentUser, "SEARCH_INVENTORY");
                return CommonResponse.error("Access denied - Cannot search inventory", "INV_048");
            }

            if (query == null || query.trim().isEmpty()) {
                return CommonResponse.error("Search query is required", "INV_049");
            }

            List<Medication> medications = List.of();
            List<ProcedureType> procedures = List.of();
            List<DiagnosticTest> diagnostics = List.of();

            // Buscar en todos los tipos si no se especifica tipo
            if (type == null || type.isEmpty() || "MEDICATION".equals(type)) {
                medications = inventoryService.searchMedications(query);
            }
            if (type == null || type.isEmpty() || "PROCEDURE".equals(type)) {
                procedures = inventoryService.searchProcedures(query);
            }
            if (type == null || type.isEmpty() || "DIAGNOSTIC".equals(type)) {
                diagnostics = inventoryService.searchDiagnostics(query);
            }

            // Combinar resultados
            List<InventoryResponse> results = new java.util.ArrayList<>();
            results.addAll(inventoryMapper.toMedicationListResponse(medications).getItems());
            results.addAll(inventoryMapper.toProcedureListResponse(procedures).getItems());
            results.addAll(inventoryMapper.toDiagnosticListResponse(diagnostics).getItems());

            logInventorySearched(query, results.size(), currentUser);

            return CommonResponse.success("Search completed successfully", results);

        } catch (Exception e) {
            logSystemError("searchInventory", e, currentUser);
            return CommonResponse.error("Internal error searching inventory", "INV_050");
        }
    }

    public CommonResponse<List<InventoryResponse>> getLowStockItems(int threshold, AuthenticatedUser currentUser) {
        try {
            if (!canManageInventory(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_LOW_STOCK");
                return CommonResponse.error("Access denied - Cannot view low stock items", "INV_051");
            }

            List<Medication> lowStockMedications = inventoryService.getLowStockMedications(threshold);
            List<InventoryResponse> results = inventoryMapper.toMedicationListResponse(lowStockMedications).getItems();

            logLowStockViewed(results.size(), threshold, currentUser);

            return CommonResponse.success(
                String.format("Found %d items with stock below %d", results.size(), threshold),
                results
            );

        } catch (Exception e) {
            logSystemError("getLowStockItems", e, currentUser);
            return CommonResponse.error("Internal error retrieving low stock items", "INV_052");
        }
    }

    private void logInventorySearched(String query, int resultsCount, AuthenticatedUser currentUser) {
        System.out.printf("INVENTORY SEARCH: %s searched for '%s' - found %d results at %s%n",
            currentUser.getFullName(), query, resultsCount, java.time.LocalDateTime.now());
    }

    private void logLowStockViewed(int itemCount, int threshold, AuthenticatedUser currentUser) {
        System.out.printf("LOW STOCK VIEWED: %s viewed %d items with stock below %d at %s%n",
            currentUser.getFullName(), itemCount, threshold, java.time.LocalDateTime.now());
    }
}