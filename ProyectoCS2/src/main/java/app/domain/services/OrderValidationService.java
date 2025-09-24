package app.domain.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import app.domain.model.DiagnosticOrderItem;
import app.domain.model.MedicationOrderItem;
import app.domain.model.OrderItem;
import app.domain.model.ProcedureOrderItem;

public class OrderValidationService {
    
    public void validateOrderRules(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        
        validateDiagnosticRules(items);
        validateItemNumberUniqueness(items);
        validateMixedOrderRules(items);
        validateOrderNumberConsistency(items);
    }
    
    /**
     * REGLA CRÍTICA DEL DOCUMENTO:
     * "Cuando se receta una ayuda diagnóstica no puede recetarse procedimiento ni 
     * medicamento ya que no se tiene certeza del diagnóstico"
     */
    private void validateDiagnosticRules(List<OrderItem> items) {
        boolean hasDiagnostic = items.stream()
            .anyMatch(item -> item instanceof DiagnosticOrderItem);
            
        boolean hasMedicationOrProcedure = items.stream()
            .anyMatch(item -> item instanceof MedicationOrderItem || 
                                item instanceof ProcedureOrderItem);
        
        if (hasDiagnostic && hasMedicationOrProcedure) {
            throw new IllegalArgumentException(
                "Cannot mix diagnostic aids with medications or procedures in same order - " +
                "diagnostic certainty required first");
        }
    }
    
    /**
     * REGLA DEL DOCUMENTO:
     * "No puede existir dos elementos dentro de la misma orden que correspondan al mismo ítem"
     * "La relación orden – ítem debe ser única"
     */
    private void validateItemNumberUniqueness(List<OrderItem> items) {
        Set<Integer> itemNumbers = items.stream()
            .map(OrderItem::getItemNumber)
            .collect(Collectors.toSet());
            
        if (itemNumbers.size() != items.size()) {
            throw new IllegalArgumentException("Item numbers must be unique within order");
        }
        
        // Validar que los números de ítem empiecen en 1
        boolean hasItemOne = items.stream()
            .anyMatch(item -> item.getItemNumber() == 1);
            
        if (!hasItemOne) {
            throw new IllegalArgumentException("Item numbers must start at 1");
        }
        
        // Validar secuencia continua (opcional pero recomendado)
        int maxItemNumber = items.stream()
            .mapToInt(OrderItem::getItemNumber)
            .max()
            .orElse(0);
            
        if (maxItemNumber != items.size()) {
            throw new IllegalArgumentException("Item numbers should be consecutive starting from 1");
        }
    }
    
    /**
     * REGLAS DEL DOCUMENTO:
     * "En caso de haber varios medicamentos recetados, todos van asociados a la misma orden"
     * "En caso de haber varios procedimientos solicitados, todos van asociados a la misma orden"
     * "Las ordenes deben ser únicas, no se repite un identificador de orden"
     */
    private void validateMixedOrderRules(List<OrderItem> items) {
        // Validar que todos los items pertenecen a la misma orden
        Set<String> orderNumbers = items.stream()
            .map(OrderItem::getOrderNumber)
            .collect(Collectors.toSet());
            
        if (orderNumbers.size() > 1) {
            throw new IllegalArgumentException("All items must belong to the same order number");
        }
        
        // Validar reglas específicas por tipo
        validateMedicationOrderRules(items);
        validateProcedureOrderRules(items);
        validateDiagnosticOrderRules(items);
    }
    
    private void validateMedicationOrderRules(List<OrderItem> items) {
        List<MedicationOrderItem> medications = items.stream()
            .filter(item -> item instanceof MedicationOrderItem)
            .map(item -> (MedicationOrderItem) item)
            .toList();
            
        if (medications.isEmpty()) return;
        
        // Todos los medicamentos deben tener el mismo número de orden
        Set<String> medicationOrders = medications.stream()
            .map(MedicationOrderItem::getOrderNumber)
            .collect(Collectors.toSet());
            
        if (medicationOrders.size() > 1) {
            throw new IllegalArgumentException("All medications must belong to same order");
        }
    }
    
    private void validateProcedureOrderRules(List<OrderItem> items) {
        List<ProcedureOrderItem> procedures = items.stream()
            .filter(item -> item instanceof ProcedureOrderItem)
            .map(item -> (ProcedureOrderItem) item)
            .toList();
            
        if (procedures.isEmpty()) return;
        
        // Todos los procedimientos deben tener el mismo número de orden
        Set<String> procedureOrders = procedures.stream()
            .map(ProcedureOrderItem::getOrderNumber)
            .collect(Collectors.toSet());
            
        if (procedureOrders.size() > 1) {
            throw new IllegalArgumentException("All procedures must belong to same order");
        }
    }
    
    private void validateDiagnosticOrderRules(List<OrderItem> items) {
        List<DiagnosticOrderItem> diagnostics = items.stream()
            .filter(item -> item instanceof DiagnosticOrderItem)
            .map(item -> (DiagnosticOrderItem) item)
            .toList();
            
        if (diagnostics.isEmpty()) return;
        
        // Todas las ayudas diagnósticas deben tener el mismo número de orden
        Set<String> diagnosticOrders = diagnostics.stream()
            .map(DiagnosticOrderItem::getOrderNumber)
            .collect(Collectors.toSet());
            
        if (diagnosticOrders.size() > 1) {
            throw new IllegalArgumentException("All diagnostic aids must belong to same order");
        }
    }
    
    private void validateOrderNumberConsistency(List<OrderItem> items) {
        // Validar formato de número de orden (máximo 6 dígitos)
        boolean hasInvalidOrderNumber = items.stream()
            .anyMatch(item -> !item.getOrderNumber().matches("\\d{1,6}"));
            
        if (hasInvalidOrderNumber) {
            throw new IllegalArgumentException("Order number must be numeric with maximum 6 digits");
        }
    }
    
    /**
     * Validaciones adicionales específicas del dominio médico
     */
    public void validateSpecialtyRequirements(List<OrderItem> items) {
        // Validar que cuando se requiere especialista, se especifique la especialidad
        boolean hasInvalidSpecialtyRequirement = items.stream()
            .anyMatch(this::hasInvalidSpecialtyRequirement);
            
        if (hasInvalidSpecialtyRequirement) {
            throw new IllegalArgumentException(
                "When specialist is required, specialty ID must be provided");
        }
    }
    
    private boolean hasInvalidSpecialtyRequirement(OrderItem item) {
        if (item instanceof ProcedureOrderItem) {
            ProcedureOrderItem proc = (ProcedureOrderItem) item;
            return proc.isSpecialistRequired() && 
                    (proc.getSpecialtyId() == null || proc.getSpecialtyId().isBlank());
        }
        
        if (item instanceof DiagnosticOrderItem) {
            DiagnosticOrderItem diag = (DiagnosticOrderItem) item;
            return diag.isSpecialistRequired() && 
                    (diag.getSpecialtyId() == null || diag.getSpecialtyId().isBlank());
        }
        
        return false;
    }
}