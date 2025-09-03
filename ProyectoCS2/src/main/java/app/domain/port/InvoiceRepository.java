package app.domain.port;

import app.domain.model.Invoice;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);
    String nextInvoiceId();
}