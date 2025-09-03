package app.application.usecase;

import java.util.List;

import app.domain.model.DiagnosticOrderItem;
import app.domain.services.DoctorService;

public class AddDiagnosticToOrderUseCase {
    private final DoctorService service;

    public AddDiagnosticToOrderUseCase(DoctorService service) {
        this.service = service;
    }

    public void execute(String orderNumber, List<DiagnosticOrderItem> items) {
        service.addDiagnosticItems(orderNumber, items);
    }
}