package app.adapter.out.persistence;

import app.domain.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataInvoiceRepository extends JpaRepository<Invoice, String> {
    List<Invoice> findByPatientId(String patientId);
}
