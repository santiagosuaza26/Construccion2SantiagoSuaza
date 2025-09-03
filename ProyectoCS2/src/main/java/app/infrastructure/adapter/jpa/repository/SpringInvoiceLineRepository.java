package app.infrastructure.adapter.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.infrastructure.adapter.jpa.entity.InvoiceLineEntity;

public interface SpringInvoiceLineRepository extends JpaRepository<InvoiceLineEntity, Long> {
}