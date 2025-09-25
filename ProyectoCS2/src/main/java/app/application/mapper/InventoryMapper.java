package app.application.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.application.dto.request.InventoryItemRequest;
import app.application.dto.response.InventoryResponse;
import app.domain.model.DiagnosticTest;
import app.domain.model.Medication;
import app.domain.model.ProcedureType;
import app.domain.model.Specialty;

/**
 * InventoryMapper - Mapper profesional para gestión de inventarios
 * 
 * RESPONSABILIDADES:
 * - Convertir InventoryItemRequest → Domain Models (Medication, Procedure, etc.)
 * - Convertir Domain Models → InventoryResponse
 * - Manejar diferentes tipos de inventario (medications, procedures, diagnostics, specialties)
 * - Validar y procesar operaciones de stock
 * - Aplicar reglas específicas de inventario médico
 * 
 * REGLAS DE INVENTARIO IMPLEMENTADAS:
 * - Solo medicamentos tienen stock
 * - Procedimientos y diagnósticos son servicios (sin stock físico)
 * - Especialidades son catálogos (sin precio ni stock)
 * - Operaciones de stock solo para personal de soporte
 * - Validaciones de precios y cantidades
 */
@Component
public class InventoryMapper {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // =============================================================================
    // CONVERSIONES REQUEST → DOMAIN MODELS
    // =============================================================================
    
    /**
     * Convierte InventoryItemRequest → Medication (Domain Model)
     * 
     * VALIDACIONES ESPECÍFICAS:
     * - Precio obligatorio y positivo
     * - Stock obligatorio para medicamentos
     * - ID único de medicamento
     */
    public Medication toMedication(InventoryItemRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("InventoryItemRequest cannot be null");
        }
        
        validateMedicationRequest(request);
        
        try {
            return new Medication(
                request.getItemId().trim(),
                request.getName().trim(),
                request.getDescription() != null ? request.getDescription().trim() : "",
                request.getStock() != null ? request.getStock() : 0,
                request.getPrice() != null ? request.getPrice() : 0L
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating Medication: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte InventoryItemRequest → ProcedureType (Domain Model)
     * 
     * VALIDACIONES ESPECÍFICAS:
     * - Precio obligatorio para servicios
     * - Sin stock (es un servicio)
     * - Descripción detallada recomendada
     */
    public ProcedureType toProcedureType(InventoryItemRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("InventoryItemRequest cannot be null");
        }
        
        validateProcedureRequest(request);
        
        try {
            return new ProcedureType(
                request.getItemId().trim(),
                request.getName().trim(),
                request.getDescription() != null ? request.getDescription().trim() : "",
                request.getPrice() != null ? request.getPrice() : 0L
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating ProcedureType: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte InventoryItemRequest → DiagnosticTest (Domain Model)
     */
    public DiagnosticTest toDiagnosticTest(InventoryItemRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("InventoryItemRequest cannot be null");
        }
        
        validateDiagnosticRequest(request);
        
        try {
            return new DiagnosticTest(
                request.getItemId().trim(),
                request.getName().trim(),
                request.getDescription() != null ? request.getDescription().trim() : "",
                request.getPrice() != null ? request.getPrice() : 0L
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating DiagnosticTest: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte InventoryItemRequest → Specialty (Domain Model)
     * 
     * CARACTERÍSTICAS ESPECIALES:
     * - Sin precio (es un catálogo)
     * - Sin stock (es información)
     * - Descripción detallada importante
     */
    public Specialty toSpecialty(InventoryItemRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("InventoryItemRequest cannot be null");
        }
        
        validateSpecialtyRequest(request);
        
        try {
            return new Specialty(
                request.getItemId().trim(),
                request.getName().trim(),
                request.getDescription() != null ? request.getDescription().trim() : ""
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating Specialty: " + e.getMessage(), e);
        }
    }
    
    // =============================================================================
    // CONVERSIONES DOMAIN MODELS → RESPONSE
    // =============================================================================
    
    /**
     * Convierte Medication → InventoryResponse
     */
    public InventoryResponse toInventoryResponse(Medication medication) {
        if (medication == null) {
            throw new IllegalArgumentException("Medication cannot be null");
        }
        
        try {
            return new InventoryResponse(
                "MEDICATION",
                medication.getMedicationId(),
                medication.getName(),
                medication.getDescription(),
                medication.getPrice(),
                medication.getStock(),
                LocalDate.now().format(DATE_FORMATTER)
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting Medication to response: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte ProcedureType → InventoryResponse
     */
    public InventoryResponse toInventoryResponse(ProcedureType procedure) {
        if (procedure == null) {
            throw new IllegalArgumentException("ProcedureType cannot be null");
        }
        
        try {
            return new InventoryResponse(
                "PROCEDURE",
                procedure.getProcedureId(),
                procedure.getName(),
                procedure.getDescription(),
                procedure.getPrice()
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting ProcedureType to response: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte DiagnosticTest → InventoryResponse
     */
    public InventoryResponse toInventoryResponse(DiagnosticTest diagnostic) {
        if (diagnostic == null) {
            throw new IllegalArgumentException("DiagnosticTest cannot be null");
        }
        
        try {
            return new InventoryResponse(
                "DIAGNOSTIC",
                diagnostic.getDiagnosticId(),
                diagnostic.getName(),
                diagnostic.getDescription(),
                diagnostic.getPrice()
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting DiagnosticTest to response: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte Specialty → InventoryResponse
     */
    public InventoryResponse toInventoryResponse(Specialty specialty) {
        if (specialty == null) {
            throw new IllegalArgumentException("Specialty cannot be null");
        }
        
        try {
            return new InventoryResponse(
                "SPECIALTY",
                specialty.getSpecialtyId(),
                specialty.getName(),
                specialty.getDescription(),
                0L // Specialties don't have price
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting Specialty to response: " + e.getMessage(), e);
        }
    }
    
    // =============================================================================
    // CONVERSIONES PARA LISTAS DE INVENTARIO
    // =============================================================================
    
    /**
     * Convierte lista de Medications → InventoryResponse con lista de items
     */
    public InventoryResponse toMedicationListResponse(List<Medication> medications) {
        if (medications == null) {
            throw new IllegalArgumentException("Medication list cannot be null");
        }
        
        List<InventoryResponse.InventoryItemInfo> itemInfos = medications.stream()
            .map(this::toMedicationItemInfo)
            .collect(Collectors.toList());
        
        return new InventoryResponse("MEDICATION", itemInfos, itemInfos.size());
    }
    
    /**
     * Convierte lista de ProcedureTypes → InventoryResponse con lista
     */
    public InventoryResponse toProcedureListResponse(List<ProcedureType> procedures) {
        if (procedures == null) {
            throw new IllegalArgumentException("Procedure list cannot be null");
        }
        
        List<InventoryResponse.InventoryItemInfo> itemInfos = procedures.stream()
            .map(this::toProcedureItemInfo)
            .collect(Collectors.toList());
        
        return new InventoryResponse("PROCEDURE", itemInfos, itemInfos.size());
    }
    
    /**
     * Convierte lista de DiagnosticTests → InventoryResponse con lista
     */
    public InventoryResponse toDiagnosticListResponse(List<DiagnosticTest> diagnostics) {
        if (diagnostics == null) {
            throw new IllegalArgumentException("Diagnostic list cannot be null");
        }
        
        List<InventoryResponse.InventoryItemInfo> itemInfos = diagnostics.stream()
            .map(this::toDiagnosticItemInfo)
            .collect(Collectors.toList());
        
        return new InventoryResponse("DIAGNOSTIC", itemInfos, itemInfos.size());
    }
    
    /**
     * Convierte lista de Specialties → InventoryResponse con lista
     */
    public InventoryResponse toSpecialtyListResponse(List<Specialty> specialties) {
        if (specialties == null) {
            throw new IllegalArgumentException("Specialty list cannot be null");
        }
        
        List<InventoryResponse.InventoryItemInfo> itemInfos = specialties.stream()
            .map(this::toSpecialtyItemInfo)
            .collect(Collectors.toList());
        
        return new InventoryResponse("SPECIALTY", itemInfos, itemInfos.size());
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE UTILIDAD PARA VALIDACIONES
    // =============================================================================
    
    private void validateMedicationRequest(InventoryItemRequest request) {
        if (!request.isMedication()) {
            throw new IllegalArgumentException("Request must be for MEDICATION type");
        }
        
        if (request.getItemId() == null || request.getItemId().isBlank()) {
            throw new IllegalArgumentException("Medication ID is required");
        }
        
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Medication name is required");
        }
        
        if (request.getPrice() == null || request.getPrice() < 0) {
            throw new IllegalArgumentException("Medication price must be positive");
        }
        
        if (request.getStock() == null || request.getStock() < 0) {
            throw new IllegalArgumentException("Medication stock must be non-negative");
        }
    }
    
    private void validateProcedureRequest(InventoryItemRequest request) {
        if (!request.isProcedure()) {
            throw new IllegalArgumentException("Request must be for PROCEDURE type");
        }
        
        if (request.getItemId() == null || request.getItemId().isBlank()) {
            throw new IllegalArgumentException("Procedure ID is required");
        }
        
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Procedure name is required");
        }
        
        if (request.getPrice() == null || request.getPrice() < 0) {
            throw new IllegalArgumentException("Procedure price must be positive");
        }
        
        // Procedures don't have stock
        if (request.getStock() != null) {
            throw new IllegalArgumentException("Procedures should not have stock information");
        }
    }
    
    private void validateDiagnosticRequest(InventoryItemRequest request) {
        if (!request.isDiagnostic()) {
            throw new IllegalArgumentException("Request must be for DIAGNOSTIC type");
        }
        
        if (request.getItemId() == null || request.getItemId().isBlank()) {
            throw new IllegalArgumentException("Diagnostic test ID is required");
        }
        
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Diagnostic test name is required");
        }
        
        if (request.getPrice() == null || request.getPrice() < 0) {
            throw new IllegalArgumentException("Diagnostic test price must be positive");
        }
        
        // Diagnostic tests don't have stock
        if (request.getStock() != null) {
            throw new IllegalArgumentException("Diagnostic tests should not have stock information");
        }
    }
    
    private void validateSpecialtyRequest(InventoryItemRequest request) {
        if (!request.isSpecialty()) {
            throw new IllegalArgumentException("Request must be for SPECIALTY type");
        }
        
        if (request.getItemId() == null || request.getItemId().isBlank()) {
            throw new IllegalArgumentException("Specialty ID is required");
        }
        
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Specialty name is required");
        }
        
        // Specialties don't have price or stock
        if (request.getPrice() != null && request.getPrice() > 0) {
            throw new IllegalArgumentException("Specialties should not have price information");
        }
        
        if (request.getStock() != null) {
            throw new IllegalArgumentException("Specialties should not have stock information");
        }
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS PARA CREAR ITEM INFOS
    // =============================================================================
    
    private InventoryResponse.InventoryItemInfo toMedicationItemInfo(Medication medication) {
        String stockStatus = determineStockStatus(medication.getStock());
        boolean available = medication.getStock() > 0;
        
        return new InventoryResponse.InventoryItemInfo(
            "MEDICATION",
            medication.getMedicationId(),
            medication.getName(),
            medication.getDescription(),
            medication.getPrice(),
            medication.getStock(),
            stockStatus,
            available,
            LocalDate.now().format(DATE_FORMATTER)
        );
    }
    
    private InventoryResponse.InventoryItemInfo toProcedureItemInfo(ProcedureType procedure) {
        return new InventoryResponse.InventoryItemInfo(
            "PROCEDURE",
            procedure.getProcedureId(),
            procedure.getName(),
            procedure.getPrice(),
            true // Procedures are always available (services)
        );
    }
    
    private InventoryResponse.InventoryItemInfo toDiagnosticItemInfo(DiagnosticTest diagnostic) {
        return new InventoryResponse.InventoryItemInfo(
            "DIAGNOSTIC",
            diagnostic.getDiagnosticId(),
            diagnostic.getName(),
            diagnostic.getPrice(),
            true // Diagnostic tests are always available (services)
        );
    }
    
    private InventoryResponse.InventoryItemInfo toSpecialtyItemInfo(Specialty specialty) {
        return new InventoryResponse.InventoryItemInfo(
            "SPECIALTY",
            specialty.getSpecialtyId(),
            specialty.getName(),
            0L, // Specialties don't have price
            true // Specialties are always available (catalog)
        );
    }
    
    private String determineStockStatus(Integer stock) {
        if (stock == null || stock <= 0) {
            return "OUT_OF_STOCK";
        } else if (stock <= 10) {
            return "LOW_STOCK";
        } else {
            return "IN_STOCK";
        }
    }
    
    // =============================================================================
    // MÉTODOS DE UTILIDAD PARA OPERACIONES DE STOCK
    // =============================================================================
    
    /**
     * Validar operación de actualización de stock
     */
    public void validateStockOperation(InventoryItemRequest request, Medication currentMedication) {
        if (request == null) {
            throw new IllegalArgumentException("Stock update request cannot be null");
        }
        
        if (currentMedication == null) {
            throw new IllegalArgumentException("Current medication cannot be null");
        }
        
        if (!request.isStockOperation()) {
            throw new IllegalArgumentException("Request must be a stock operation");
        }
        
        if (request.getStockChange() == null || request.getStockChange() <= 0) {
            throw new IllegalArgumentException("Stock change must be positive");
        }
        
        // Si es operación de substracción, validar que hay suficiente stock
        if (request.isSubtractOperation()) {
            if (currentMedication.getStock() < request.getStockChange()) {
                throw new IllegalArgumentException(
                    String.format("Insufficient stock. Available: %d, Requested: %d", 
                        currentMedication.getStock(), request.getStockChange())
                );
            }
        }
    }
    
    /**
     * Calcular nuevo stock después de operación
     */
    public int calculateNewStock(Medication currentMedication, InventoryItemRequest stockOperation) {
        validateStockOperation(stockOperation, currentMedication);
        
        if (stockOperation.isAddOperation()) {
            return currentMedication.getStock() + stockOperation.getStockChange();
        } else if (stockOperation.isSubtractOperation()) {
            return currentMedication.getStock() - stockOperation.getStockChange();
        } else {
            throw new IllegalArgumentException("Invalid stock operation type");
        }
    }
    
    // =============================================================================
    // MÉTODOS DE UTILIDAD PARA TESTING Y DEBUGGING
    // =============================================================================
    
    /**
     * Crear request de prueba para medicamento
     */
    public InventoryItemRequest createTestMedicationRequest(String id, String name, int stock, long price) {
        return new InventoryItemRequest("MEDICATION", id, name, "Test medication", price, stock);
    }
    
    /**
     * Crear request de prueba para procedimiento
     */
    public InventoryItemRequest createTestProcedureRequest(String id, String name, long price) {
        return new InventoryItemRequest("PROCEDURE", id, name, "Test procedure", price);
    }
    
    /**
     * Método para debugging de inventario
     */
    public String inventoryItemToDebugString(Object item) {
        if (item == null) return "InventoryItem[null]";
        
        if (item instanceof Medication) {
            Medication med = (Medication) item;
            return String.format("Medication[id=%s, name=%s, stock=%d, price=%s]",
                med.getMedicationId(), med.getName(), med.getStock(), 
                String.format("$%,d", med.getPrice()));
        } else if (item instanceof ProcedureType) {
            ProcedureType proc = (ProcedureType) item;
            return String.format("Procedure[id=%s, name=%s, price=%s]",
                proc.getProcedureId(), proc.getName(), 
                String.format("$%,d", proc.getPrice()));
        } else if (item instanceof DiagnosticTest) {
            DiagnosticTest diag = (DiagnosticTest) item;
            return String.format("Diagnostic[id=%s, name=%s, price=%s]",
                diag.getDiagnosticId(), diag.getName(), 
                String.format("$%,d", diag.getPrice()));
        } else if (item instanceof Specialty) {
            Specialty spec = (Specialty) item;
            return String.format("Specialty[id=%s, name=%s]",
                spec.getSpecialtyId(), spec.getName());
        } else {
            return "Unknown[" + item.getClass().getSimpleName() + "]";
        }
    }
}