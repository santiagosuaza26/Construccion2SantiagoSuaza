package app.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.domain.model.Invoice;
import app.domain.repository.InvoiceRepository;

@Repository
public class JpaInvoiceRepositoryAdapter implements InvoiceRepository {

    private final SpringDataInvoiceRepository jpa;

    public JpaInvoiceRepositoryAdapter(SpringDataInvoiceRepository jpa) {
        this.jpa = jpa;
    }

    @Override public void save(Invoice invoice) { jpa.save(invoice); }
    @Override public Optional<Invoice> findById(String id) { return jpa.findById(id); }
    @Override public List<Invoice> findAll() { return jpa.findAll(); }
    @Override public void deleteById(String id) { jpa.deleteById(id); }
}
