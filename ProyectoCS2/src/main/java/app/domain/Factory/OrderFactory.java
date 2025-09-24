package app.domain.factory;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import app.domain.model.DiagnosticOrderItem;
import app.domain.model.MedicationOrderItem;
import app.domain.model.OrderHeader;
import app.domain.model.OrderItem;
import app.domain.model.ProcedureOrderItem;
import app.domain.port.OrderHeaderRepository;

public class OrderFactory {
    private final OrderHeaderRepository orderHeaderRepository;
    
    public OrderFactory(OrderHeaderRepository orderHeaderRepository) {
        this.orderHeaderRepository = orderHeaderRepository;
    }
    
    public String generateUniqueOrderNumber() {
        String orderNumber;
        do {
            orderNumber = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        } while (orderHeaderRepository.existsOrderNumber(orderNumber));
        return orderNumber;
    }
    
    public OrderHeader createOrderHeader(String patientIdCard, String doctorIdCard) {
        String orderNumber = generateUniqueOrderNumber();
        return new OrderHeader(orderNumber, patientIdCard, doctorIdCard, LocalDate.now());
    }
    
    // Validar que no hay items duplicados y que siguen reglas de negocio
    public void validateOrderItems(List<OrderItem> items) {
        boolean hasDiagnostic = items.stream()
            .anyMatch(item -> item instanceof DiagnosticOrderItem);
        
        boolean hasMedicationOrProcedure = items.stream()
            .anyMatch(item -> item instanceof MedicationOrderItem || 
                                item instanceof ProcedureOrderItem);
        
        // Regla: no se puede recetar medicamento/procedimiento con ayuda diagnóstica
        if (hasDiagnostic && hasMedicationOrProcedure) {
            throw new IllegalArgumentException(
                "Cannot prescribe medications or procedures with diagnostic aids");
        }
        
        // Validar que no hay números de ítem duplicados
        long distinctItems = items.stream()
            .mapToInt(OrderItem::getItemNumber)
            .distinct()
            .count();
            
        if (distinctItems != items.size()) {
            throw new IllegalArgumentException("Duplicate item numbers in order");
        }
    }
}