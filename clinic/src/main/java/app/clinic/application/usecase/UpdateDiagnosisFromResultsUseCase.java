package app.clinic.application.usecase;

import java.util.List;

import app.clinic.domain.model.entities.Order;
import app.clinic.domain.service.OrderService;

public class UpdateDiagnosisFromResultsUseCase {
    private final OrderService orderService;

    public UpdateDiagnosisFromResultsUseCase(OrderService orderService) {
        this.orderService = orderService;
    }

    public Order execute(String patientId, String doctorId, String diagnosis,
                        List<app.clinic.domain.model.entities.MedicationOrder> medications,
                        List<app.clinic.domain.model.entities.ProcedureOrder> procedures) {
        return orderService.createPostDiagnosticOrder(patientId, doctorId, diagnosis, medications, procedures);
    }
}