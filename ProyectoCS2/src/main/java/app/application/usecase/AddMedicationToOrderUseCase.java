package app.application.usecase;

import com.clinic.domain.model.MedicationOrderItem;
import com.clinic.domain.service.DoctorService;

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