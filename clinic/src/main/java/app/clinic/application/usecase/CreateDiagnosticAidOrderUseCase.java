package app.clinic.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.DiagnosticAidOrder;
import app.clinic.domain.model.entities.Order;
import app.clinic.domain.service.OrderService;

@Service
public class CreateDiagnosticAidOrderUseCase {
    private final OrderService orderService;

    public CreateDiagnosticAidOrderUseCase(OrderService orderService) {
        this.orderService = orderService;
    }

    public Order execute(String patientId, String doctorId, List<DiagnosticAidOrder> diagnosticAids) {
        return orderService.createStandaloneDiagnosticAidOrder(patientId, doctorId, diagnosticAids);
    }
}