package app.domain.port;

import java.time.LocalDate;
import java.util.List;

import app.domain.model.Invoice;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);
    String nextInvoiceId();
    List<Invoice> findByPatientAndYear(String patientIdCard, int year);
    List<Invoice> findByDateRange(LocalDate start, LocalDate end);
    long count();
    long countInvoicesThisMonth();
    double getAverageInvoiceAmount();

    // Métodos adicionales necesarios para el servicio
    java.util.Optional<Invoice> findById(String invoiceId);
    List<Invoice> findByPatientIdCard(String patientIdCard);
    List<Invoice> findAll();
}