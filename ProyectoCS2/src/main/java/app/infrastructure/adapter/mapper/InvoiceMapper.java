package app.infrastructure.adapter.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.domain.model.Invoice;
import app.domain.model.InvoiceLine;
import app.infrastructure.adapter.jpa.entity.InvoiceEntity;
import app.infrastructure.adapter.jpa.entity.InvoiceLineEntity;

@Component("infrastructureInvoiceMapper")
public class InvoiceMapper {

    public InvoiceEntity toEntity(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        InvoiceEntity entity = new InvoiceEntity();
        entity.setInvoiceId(invoice.getInvoiceId());
        entity.setPatientName(invoice.getPatientName());
        entity.setPatientAge(invoice.getPatientAge());
        entity.setPatientIdCard(invoice.getPatientIdCard());
        entity.setDoctorName(invoice.getDoctorName());
        entity.setInsuranceCompany(invoice.getInsuranceCompany());
        entity.setPolicyNumber(invoice.getPolicyNumber());
        entity.setPolicyRemainingDays(invoice.getPolicyRemainingDays());
        entity.setPolicyEndDate(invoice.getPolicyEndDate());
        entity.setCopay(invoice.getCopay());
        entity.setTotal(invoice.getTotal());

        // Convertir líneas
        List<InvoiceLineEntity> lineEntities = invoice.getLines().stream()
            .map(this::toLineEntity)
            .collect(Collectors.toList());

        for (InvoiceLineEntity lineEntity : lineEntities) {
            lineEntity.setInvoice(entity);
        }

        entity.setLines(lineEntities);

        return entity;
    }

    public Invoice toDomain(InvoiceEntity entity) {
        if (entity == null) {
            return null;
        }

        List<InvoiceLine> lines = entity.getLines().stream()
            .map(this::toLineDomain)
            .collect(Collectors.toList());

        return new Invoice(
            entity.getInvoiceId(),
            entity.getPatientName(),
            entity.getPatientAge(),
            entity.getPatientIdCard(),
            entity.getDoctorName(),
            entity.getInsuranceCompany(),
            entity.getPolicyNumber(),
            entity.getPolicyRemainingDays(),
            entity.getPolicyEndDate(),
            lines,
            entity.getCopay(),
            entity.getTotal()
        );
    }

    private InvoiceLineEntity toLineEntity(InvoiceLine line) {
        InvoiceLineEntity entity = new InvoiceLineEntity();
        entity.setDescription(line.getDescription());
        entity.setCost(line.getAmount());
        return entity;
    }

    private InvoiceLine toLineDomain(InvoiceLineEntity entity) {
        return new InvoiceLine(entity.getDescription(), entity.getCost());
    }
}