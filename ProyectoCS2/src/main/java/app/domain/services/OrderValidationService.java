package app.domain.services;

import java.util.List;

import app.domain.model.DiagnosticOrderItem;
import app.domain.model.MedicationOrderItem;
import app.domain.model.OrderItem;
import app.domain.model.ProcedureOrderItem;

public class OrderValidationService {
    
    public void validateOrderRules(List<OrderItem> items) {
        validateDiagnosticRules(items);
        validateItemNumberUniqueness(items);
        validateMixedOrderRules(items);
    }
    
    private void validateDiagnosticRules(List<OrderItem> items) {
        boolean hasDiagnostic = items.stream()
            .anyMatch(item -> item instanceof DiagnosticOrderItem);
            
        boolean hasMedicationOrProcedure = items.stream()
            .anyMatch(item -> item instanceof MedicationOrderItem || 
                                item instanceof ProcedureOrderItem);
        
        if (hasDiagnostic && hasMedicationOrProcedure) {
            throw new IllegalArgumentException(
                "Cannot mix diagnostic aids with medications or procedures in same order");
        }
    }
    
    private void validateItemNumberUniqueness(List<OrderItem> items) {
        long distinctItemNumbers = items.stream()
            .mapToInt(OrderItem::getItemNumber)
            .distinct()
            .count();
            
        if (distinctItemNumbers != items.size()) {
            throw new IllegalArgumentException("Item numbers must be unique within order");
        }
        
        // Validar que los números de ítem empiecen en 1
        boolean hasItemOne = items.stream()
            .anyMatch(item -> item.getItemNumber() == 1);
            
        if (!items.isEmpty() && !hasItemOne) {
            throw new IllegalArgumentException("Item numbers must start at 1");
        }
    }
    
    private void validateMixedOrderRules(List<OrderItem> items) {
        // Validar que cuando hay múltiples medicamentos, todos van en la misma orden
        long medicationOrders = items.stream()
            .filter(item -> item instanceof MedicationOrderItem)
            .map(OrderItem::getOrderNumber)
            .distinct()
            .count();
            
        if (medicationOrders > 1) {
            throw new IllegalArgumentException("All medications must belong to same order");
        }
        
        // Similar para procedimientos
        long procedureOrders = items.stream()
            .filter(item -> item instanceof ProcedureOrderItem)
            .map(OrderItem::getOrderNumber)
            .distinct()
            .count();
            
        if (procedureOrders > 1) {
            throw new IllegalArgumentException("All procedures must belong to same order");
        }
    }
}