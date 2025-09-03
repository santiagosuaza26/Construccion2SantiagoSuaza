package app.application.usecase;

import java.util.List;

import app.domain.model.ProcedureOrderItem;
import app.domain.services.DoctorService;

public class AddProcedureToOrderUseCase {
    private final DoctorService service;

    public AddProcedureToOrderUseCase(DoctorService service) {
        this.service = service;
    }

    public void execute(String orderNumber, List<ProcedureOrderItem> items) {
        service.addProcedureItems(orderNumber, items);
    }
}