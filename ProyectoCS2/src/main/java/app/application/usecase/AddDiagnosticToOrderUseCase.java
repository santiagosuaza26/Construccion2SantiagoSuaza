package app.application.usecase;

import app.domain.model.DiagnosticOrderItem;
import app.domain.service.DoctorService;

import java.util.List;

public class AddDiagnosticToOrderUseCase {
    private final DoctorService service;

    public AddDiagnosticToOrderUseCase(DoctorService service) {
        this.service = service;
    }

    public void execute(String orderNumber, List<DiagnosticOrderItem> items) {
        service.addDiagnosticItems(orderNumber, items);
    }
}