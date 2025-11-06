package app.clinic.infrastructure.service;

import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.entities.ProcedureOrder;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;
import app.clinic.domain.repository.OrderRepository;
import app.clinic.domain.service.OrderService;

@Service
public class ProcedureOrderServiceImpl {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public ProcedureOrderServiceImpl(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    public void addProcedureToOrder(String orderNumber, String procedureId, String quantity, String frequency, double cost, boolean requiresSpecialist, String specialistId) {
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        // Generate next item number
        int nextItem = order.getProcedures().size() + order.getMedications().size() + order.getDiagnosticAids().size() + 1;
        Id specialistIdObj = requiresSpecialist && specialistId != null ? new Id(specialistId) : null;
        ProcedureOrder procedureOrder = new ProcedureOrder(order.getOrderNumber(), nextItem, new Id(procedureId), quantity, frequency, requiresSpecialist, specialistIdObj, cost);
        order.addProcedure(procedureOrder);

        orderRepository.save(order);
    }

    public List<ProcedureOrder> getProceduresFromOrder(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        return order.getProcedures();
    }

    public void updateProcedureInOrder(String orderNumber, int itemNumber, String quantity, String frequency, double cost, boolean requiresSpecialist, String specialistId) {
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        // Find and update the procedure
        ProcedureOrder existing = order.getProcedures().stream()
                .filter(p -> p.getItem() == itemNumber)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Procedimiento no encontrado en la orden"));

        Id specialistIdObj = requiresSpecialist && specialistId != null ? new Id(specialistId) : existing.getSpecialistId();
        ProcedureOrder updated = new ProcedureOrder(order.getOrderNumber(), itemNumber, existing.getProcedureId(), quantity, frequency, requiresSpecialist, specialistIdObj, cost);
        order.getProcedures().remove(existing);
        order.getProcedures().add(updated);

        orderRepository.save(order);
    }

    public void removeProcedureFromOrder(String orderNumber, int itemNumber) {
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        order.getProcedures().removeIf(p -> p.getItem() == itemNumber);
        orderRepository.save(order);
    }
}