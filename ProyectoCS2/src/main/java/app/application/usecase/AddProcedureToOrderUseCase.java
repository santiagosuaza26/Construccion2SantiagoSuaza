package app.application.usecase;

import app.domain.model.ProcedureOrderItem;
import app.domain.service.DoctorService;

import java.util.List;

public class AddProcedureToOrderUseCase {
    private final DoctorService service;

    public AddProcedureToOrderUseCase(DoctorService service) {
        this.service = service;
    }

    public void execute(String orderNumber, List<ProcedureOrderItem> items) {
        service.addProcedureItems(orderNumber, items);
    }
}