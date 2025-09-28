package app.infrastructure.adapter.mapper;

import app.domain.model.Invoice;
import app.infrastructure.adapter.entity.InvoiceEntity;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    public InvoiceEntity toEntity(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        InvoiceEntity entity = new InvoiceEntity();
        entity.setInvoiceNumber(invoice.getInvoiceNumber());
        entity.setIssueDate(invoice.getIssueDate());
        entity.setDueDate(invoice.getDueDate());
        entity.setTotalAmount(invoice.getTotalAmount());
        entity.setStatus(invoice.getStatus());

        return entity;
    }

    public Invoice toDomain(InvoiceEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Invoice(
            entity.getInvoiceNumber(),
            entity.getIssueDate(),
            entity.getDueDate(),
            entity.getTotalAmount(),
            entity.getStatus()
        );
    }
}