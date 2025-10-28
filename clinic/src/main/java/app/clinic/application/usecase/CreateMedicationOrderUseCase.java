package app.clinic.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.MedicationOrder;
import app.clinic.domain.model.entities.Order;
import app.clinic.domain.service.OrderService;

@Service
public class CreateMedicationOrderUseCase {
    private final OrderService orderService;

    public CreateMedicationOrderUseCase(OrderService orderService) {
        this.orderService = orderService;
    }

    public Order execute(String patientId, String doctorId, List<MedicationOrder> medications) {
        return orderService.createMedicationOrder(patientId, doctorId, medications);
    }
}