package app.infrastructure.adapter.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.infrastructure.adapter.jpa.entity.InvoiceEntity;

public interface SpringInvoiceRepository extends JpaRepository<InvoiceEntity, String> {
}