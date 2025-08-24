package app.domain.repository;

import app.domain.model.Invoice;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository {
    void save(Invoice invoice);

    Optional<Invoice> findById(String id);

    List<Invoice> findAll();

    void deleteById(String id);
}
