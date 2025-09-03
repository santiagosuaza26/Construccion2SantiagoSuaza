package app.application.usecase;

import app.domain.model.MedicationOrderItem;
import app.domain.service.DoctorService;

import java.util.List;

public class AddMedicationToOrderUseCase {
    private final DoctorService service;

    public AddMedicationToOrderUseCase(DoctorService service) {
        this.service = service;
    }

    public void execute(String orderNumber, List<MedicationOrderItem> items) {
        service.addMedicationItems(orderNumber, items);
    }
}