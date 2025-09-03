package app.application.usecase;

import com.clinic.domain.model.ProcedureOrderItem;
import com.clinic.domain.service.DoctorService;

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