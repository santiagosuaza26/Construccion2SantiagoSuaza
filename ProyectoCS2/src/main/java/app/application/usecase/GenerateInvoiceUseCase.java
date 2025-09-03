package app.application.usecase;

import java.time.LocalDate;

import app.application.dto.InvoiceResponse;
import app.domain.model.Invoice;
import app.domain.model.OrderHeader;
import app.domain.services.BillingService;

public class GenerateInvoiceUseCase {
    private final BillingService service;

    public GenerateInvoiceUseCase(BillingService service) {
        this.service = service;
    }

    public InvoiceResponse execute(String orderNumber, OrderHeader header, String patientName, String doctorName) {
        Invoice invoice = service.generateInvoice(orderNumber, header, patientName, doctorName, LocalDate.now());
        return InvoiceResponse.fromDomain(invoice);
    }
}
