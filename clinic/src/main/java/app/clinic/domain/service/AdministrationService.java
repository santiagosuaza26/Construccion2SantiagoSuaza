package app.clinic.domain.service;

import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.valueobject.OrderNumber;
import app.clinic.domain.repository.OrderRepository;

public class AdministrationService {
    private final OrderRepository orderRepository;

    public AdministrationService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void administerMedication(String orderNumber, int item) {
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber)).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.getMedications().stream().filter(m -> m.getItem() == item).findFirst().orElseThrow(() -> new IllegalArgumentException("Medication item not found in order"));
        // Logic to record administration, e.g., update status or log
        // For simplicity, assume administration is recorded elsewhere
    }

    public void administerProcedure(String orderNumber, int item) {
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber)).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.getProcedures().stream().filter(p -> p.getItem() == item).findFirst().orElseThrow(() -> new IllegalArgumentException("Procedure item not found in order"));
        // Logic to record administration
    }
}