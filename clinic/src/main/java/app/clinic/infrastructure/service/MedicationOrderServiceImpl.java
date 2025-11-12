package app.clinic.infrastructure.service;

import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.MedicationOrder;
import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;
import app.clinic.domain.repository.OrderRepository;

@Service
public class MedicationOrderServiceImpl {

    private final OrderRepository orderRepository;

    public MedicationOrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void addMedicationToOrder(String orderNumber, String medicationId, String dosage, String duration, double cost) {
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        // Generate next item number
        int nextItem = order.getMedications().size() + 1;
        MedicationOrder medicationOrder = new MedicationOrder(order.getOrderNumber(), nextItem, new Id(medicationId), dosage, duration, cost);
        order.addMedication(medicationOrder);

        orderRepository.save(order);
    }

    public List<MedicationOrder> getMedicationsFromOrder(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        return order.getMedications();
    }

    public void updateMedicationInOrder(String orderNumber, int itemNumber, String dosage, String duration, double cost) {
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        // Find and update the medication
        MedicationOrder existing = order.getMedications().stream()
                .filter(m -> m.getItem() == itemNumber)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado en la orden"));

        MedicationOrder updated = new MedicationOrder(order.getOrderNumber(), itemNumber, existing.getMedicationId(), dosage, duration, cost);
        order.getMedications().remove(existing);
        order.getMedications().add(updated);

        orderRepository.save(order);
    }

    public void removeMedicationFromOrder(String orderNumber, int itemNumber) {
        Order order = orderRepository.findByOrderNumber(new OrderNumber(orderNumber))
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        order.getMedications().removeIf(m -> m.getItem() == itemNumber);
        orderRepository.save(order);
    }
}