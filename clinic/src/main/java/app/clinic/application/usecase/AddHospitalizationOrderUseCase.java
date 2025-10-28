package app.clinic.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.Order;
import app.clinic.domain.service.OrderService;

@Service
public class AddHospitalizationOrderUseCase {
    private final OrderService orderService;

    public AddHospitalizationOrderUseCase(OrderService orderService) {
        this.orderService = orderService;
    }

    public Order execute(String patientId, String doctorId, String hospitalizationDetails) {
        // Hospitalization is treated as a special procedure
        // Note: This would need to be created through the OrderService with proper OrderNumber
        // For now, we'll use the existing createProcedureOrder method
        // The actual implementation would create a hospitalization-specific order
        return orderService.createProcedureOrder(patientId, doctorId, List.of());
    }
}