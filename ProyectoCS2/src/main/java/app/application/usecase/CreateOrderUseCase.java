package app.application.usecase;

import java.time.LocalDate;

import app.domain.model.OrderHeader;
import app.domain.services.DoctorService;

public class CreateOrderUseCase {
    private final DoctorService service;

    public CreateOrderUseCase(DoctorService service) {
        this.service = service;
    }

    public OrderHeader execute(String orderNumber, String patientId, String doctorId) {
        return service.createOrderHeader(orderNumber, patientId, doctorId, LocalDate.now());
    }
}