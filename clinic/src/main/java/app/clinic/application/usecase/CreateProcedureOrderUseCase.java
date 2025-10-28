package app.clinic.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.entities.ProcedureOrder;
import app.clinic.domain.service.OrderService;

@Service
public class CreateProcedureOrderUseCase {
    private final OrderService orderService;

    public CreateProcedureOrderUseCase(OrderService orderService) {
        this.orderService = orderService;
    }

    public Order execute(String patientId, String doctorId, List<ProcedureOrder> procedures) {
        return orderService.createProcedureOrder(patientId, doctorId, procedures);
    }
}