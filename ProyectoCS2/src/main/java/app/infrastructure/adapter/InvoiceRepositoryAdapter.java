package app.infrastructure.adapter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import app.domain.model.Invoice;
import app.domain.port.InvoiceRepository;
import app.infrastructure.adapter.jpa.entity.InvoiceEntity;
import app.infrastructure.adapter.jpa.repository.SpringInvoiceRepository;
import app.infrastructure.adapter.mapper.InvoiceMapper;

@Component
public class InvoiceRepositoryAdapter implements InvoiceRepository {

    private final SpringInvoiceRepository springInvoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public InvoiceRepositoryAdapter(SpringInvoiceRepository springInvoiceRepository,
                                   @Qualifier("infrastructureInvoiceMapper") InvoiceMapper invoiceMapper) {
        this.springInvoiceRepository = springInvoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceEntity entity = invoiceMapper.toEntity(invoice);
        InvoiceEntity savedEntity = springInvoiceRepository.save(entity);
        return invoiceMapper.toDomain(savedEntity);
    }

    @Override
    public String nextInvoiceId() {
        // Generar ID único para factura
        return "INV-" + System.currentTimeMillis();
    }

    @Override
    public List<Invoice> findByPatientAndYear(String patientIdCard, int year) {
        // TODO: Implementar consulta personalizada cuando se agregue el campo patientIdCard a InvoiceEntity
        return List.of();
    }

    @Override
    public List<Invoice> findByDateRange(LocalDate start, LocalDate end) {
        // TODO: Implementar consulta personalizada cuando se agregue el campo date a InvoiceEntity
        return List.of();
    }

    @Override
    public long count() {
        return springInvoiceRepository.count();
    }

    @Override
    public long countInvoicesThisMonth() {
        // TODO: Implementar consulta personalizada para contar facturas del mes actual
        return 0L;
    }

    @Override
    public double getAverageInvoiceAmount() {
        // TODO: Implementar consulta personalizada para calcular promedio
        return 0.0;
    }
}