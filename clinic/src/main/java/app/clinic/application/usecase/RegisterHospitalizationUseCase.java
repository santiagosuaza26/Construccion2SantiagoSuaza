package app.clinic.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.Order;
import app.clinic.domain.service.OrderService;

@Service
public class RegisterHospitalizationUseCase {
    private final OrderService orderService;

    public RegisterHospitalizationUseCase(OrderService orderService) {
        this.orderService = orderService;
    }

    public Order execute(String patientId, String doctorId, List<app.clinic.domain.model.entities.ProcedureOrder> procedures) {
        // Hospitalization is treated as a special procedure
        return orderService.createProcedureOrder(patientId, doctorId, procedures);
    }
}