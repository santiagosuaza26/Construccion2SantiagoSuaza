package app.application.usecase;

import java.util.List;

import app.domain.model.MedicationOrderItem;
import app.domain.services.DoctorService;

public class AddMedicationToOrderUseCase {
    private final DoctorService service;

    public AddMedicationToOrderUseCase(DoctorService service) {
        this.service = service;
    }

    public void execute(String orderNumber, List<MedicationOrderItem> items) {
        service.addMedicationItems(orderNumber, items);
    }
}