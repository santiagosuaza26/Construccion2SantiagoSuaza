package app.clinic.infrastructure.service;

import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.DiagnosticAidOrder;
import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;
import app.clinic.domain.repository.OrderRepository;
import app.clinic.domain.service.OrderService;

@Service
public class DiagnosticOrderServiceImpl {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public DiagnosticOrderServiceImpl(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    public void addDiagnosticToOrder(String orderNumber, String diagnosticAidId, String quantity, boolean requiresSpecialist, String specialistId, double cost) {
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        // Generate next item number
        int nextItem = order.getProcedures().size() + order.getMedications().size() + order.getDiagnosticAids().size() + 1;
        Id specialistIdObj = requiresSpecialist && specialistId != null ? new Id(specialistId) : null;
        DiagnosticAidOrder diagnosticOrder = new DiagnosticAidOrder(order.getOrderNumber(), nextItem, new Id(diagnosticAidId), quantity, requiresSpecialist, specialistIdObj, cost);
        order.addDiagnosticAid(diagnosticOrder);

        orderRepository.save(order);
    }

    public List<DiagnosticAidOrder> getDiagnosticsFromOrder(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        return order.getDiagnosticAids();
    }

    public void updateDiagnosticInOrder(String orderNumber, int itemNumber, String quantity, boolean requiresSpecialist, String specialistId, double cost) {
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        // Find and update the diagnostic
        DiagnosticAidOrder existing = order.getDiagnosticAids().stream()
                .filter(d -> d.getItem() == itemNumber)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ayuda diagnóstica no encontrada en la orden"));

        Id specialistIdObj = requiresSpecialist && specialistId != null ? new Id(specialistId) : existing.getSpecialistId();
        DiagnosticAidOrder updated = new DiagnosticAidOrder(order.getOrderNumber(), itemNumber, existing.getDiagnosticAidId(), quantity, requiresSpecialist, specialistIdObj, cost);
        order.getDiagnosticAids().remove(existing);
        order.getDiagnosticAids().add(updated);

        orderRepository.save(order);
    }

    public void removeDiagnosticFromOrder(String orderNumber, int itemNumber) {
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        order.getDiagnosticAids().removeIf(d -> d.getItem() == itemNumber);
        orderRepository.save(order);
    }
}