package app.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.application.dto.InvoiceResponse;
import app.application.usecase.GenerateInvoiceUseCase;
import app.domain.model.OrderHeader;

@RestController
@RequestMapping("/api/billing")
public class BillingController {
    private final GenerateInvoiceUseCase generateInvoiceUseCase;

    public BillingController(GenerateInvoiceUseCase generateInvoiceUseCase) {
        this.generateInvoiceUseCase = generateInvoiceUseCase;
    }

    @PostMapping("/invoices")
    public ResponseEntity<InvoiceResponse> generateInvoice(@RequestBody GenerateInvoiceRequest request) {
        try {
            InvoiceResponse invoice = generateInvoiceUseCase.execute(
                request.orderNumber, 
                request.header, 
                request.patientName, 
                request.doctorName
            );
            return ResponseEntity.ok(invoice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public static class GenerateInvoiceRequest {
        public String orderNumber;
        public OrderHeader header;
        public String patientName;
        public String doctorName;
    }
}