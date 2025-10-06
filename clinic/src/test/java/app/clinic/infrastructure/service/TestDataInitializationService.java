package app.clinic.infrastructure.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import app.clinic.infrastructure.entity.InvoiceEntity;
import app.clinic.infrastructure.repository.InvoiceJpaRepository;

/**
 * Servicio para inicializar datos de prueba en entorno de testing.
 * Crea facturas de prueba para validar funcionalidades.
 */
@Service
@Profile("test")
public class TestDataInitializationService implements CommandLineRunner {

    private final InvoiceJpaRepository invoiceJpaRepository;

    public TestDataInitializationService(InvoiceJpaRepository invoiceJpaRepository) {
        this.invoiceJpaRepository = invoiceJpaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeTestInvoices();
    }

    /**
     * Inicializa facturas de prueba para testing.
     */
    public void initializeTestInvoices() {
        if (invoiceJpaRepository.count() == 0) {
            createTestInvoices();
        }
    }

    private void createTestInvoices() {
        // Crear facturas de prueba para diferentes escenarios

        // Factura 1: Paciente con copago estándar
        InvoiceEntity invoice1 = new InvoiceEntity();
        invoice1.setInvoiceNumber("TEST-2024-001");
        invoice1.setPatientCedula("12345678");
        invoice1.setTotalAmount(new BigDecimal("200000"));
        invoice1.setCopaymentAmount(new BigDecimal("50000"));
        invoice1.setInsuranceCoverage(new BigDecimal("150000"));
        invoice1.setPatientResponsibility(new BigDecimal("50000"));
        invoice1.setBillingDate(LocalDateTime.of(2024, 1, 15, 10, 0));
        invoice1.setDueDate(LocalDateTime.of(2024, 2, 15, 10, 0));
        invoice1.setStatus(InvoiceEntity.InvoiceStatus.PAID);
        invoice1.setYear(2024);

        // Factura 2: Paciente cerca del límite anual
        InvoiceEntity invoice2 = new InvoiceEntity();
        invoice2.setInvoiceNumber("TEST-2024-002");
        invoice2.setPatientCedula("12345678");
        invoice2.setTotalAmount(new BigDecimal("300000"));
        invoice2.setCopaymentAmount(new BigDecimal("75000"));
        invoice2.setInsuranceCoverage(new BigDecimal("225000"));
        invoice2.setPatientResponsibility(new BigDecimal("75000"));
        invoice2.setBillingDate(LocalDateTime.of(2024, 3, 10, 14, 30));
        invoice2.setDueDate(LocalDateTime.of(2024, 4, 10, 14, 30));
        invoice2.setStatus(InvoiceEntity.InvoiceStatus.PAID);
        invoice2.setYear(2024);

        // Factura 3: Otro paciente
        InvoiceEntity invoice3 = new InvoiceEntity();
        invoice3.setInvoiceNumber("TEST-2024-003");
        invoice3.setPatientCedula("87654321");
        invoice3.setTotalAmount(new BigDecimal("150000"));
        invoice3.setCopaymentAmount(new BigDecimal("37500"));
        invoice3.setInsuranceCoverage(new BigDecimal("112500"));
        invoice3.setPatientResponsibility(new BigDecimal("37500"));
        invoice3.setBillingDate(LocalDateTime.of(2024, 2, 20, 9, 15));
        invoice3.setDueDate(LocalDateTime.of(2024, 3, 20, 9, 15));
        invoice3.setStatus(InvoiceEntity.InvoiceStatus.PENDING);
        invoice3.setYear(2024);

        // Factura 4: Año anterior (para pruebas históricas)
        InvoiceEntity invoice4 = new InvoiceEntity();
        invoice4.setInvoiceNumber("TEST-2023-001");
        invoice4.setPatientCedula("12345678");
        invoice4.setTotalAmount(new BigDecimal("180000"));
        invoice4.setCopaymentAmount(new BigDecimal("45000"));
        invoice4.setInsuranceCoverage(new BigDecimal("135000"));
        invoice4.setPatientResponsibility(new BigDecimal("45000"));
        invoice4.setBillingDate(LocalDateTime.of(2023, 12, 5, 16, 45));
        invoice4.setDueDate(LocalDateTime.of(2024, 1, 5, 16, 45));
        invoice4.setStatus(InvoiceEntity.InvoiceStatus.PAID);
        invoice4.setYear(2023);

        // Factura 5: Paciente que excede límite (escenario límite)
        InvoiceEntity invoice5 = new InvoiceEntity();
        invoice5.setInvoiceNumber("TEST-2024-004");
        invoice5.setPatientCedula("55555555");
        invoice5.setTotalAmount(new BigDecimal("1200000")); // Servicio costoso
        invoice5.setCopaymentAmount(new BigDecimal("0"));   // Sin copago por límite excedido
        invoice5.setInsuranceCoverage(new BigDecimal("1200000")); // Seguro cubre todo
        invoice5.setPatientResponsibility(new BigDecimal("0")); // Paciente no paga
        invoice5.setBillingDate(LocalDateTime.of(2024, 6, 1, 11, 0));
        invoice5.setDueDate(LocalDateTime.of(2024, 7, 1, 11, 0));
        invoice5.setStatus(InvoiceEntity.InvoiceStatus.PAID);
        invoice5.setYear(2024);

        // Guardar todas las facturas
        invoiceJpaRepository.saveAll(List.of(invoice1, invoice2, invoice3, invoice4, invoice5));
    }

    /**
     * Crea datos de prueba adicionales para escenarios específicos.
     */
    public void createAdditionalTestData() {
        // Crear más facturas para pruebas de carga
        for (int i = 5; i <= 10; i++) {
            InvoiceEntity invoice = new InvoiceEntity();
            invoice.setInvoiceNumber("TEST-2024-00" + i);
            invoice.setPatientCedula("12345678");
            invoice.setTotalAmount(new BigDecimal("100000"));
            invoice.setCopaymentAmount(new BigDecimal("25000"));
            invoice.setInsuranceCoverage(new BigDecimal("75000"));
            invoice.setPatientResponsibility(new BigDecimal("25000"));
            invoice.setBillingDate(LocalDateTime.now().minusDays(i));
            invoice.setDueDate(LocalDateTime.now().minusDays(i).plusDays(30));
            invoice.setStatus(InvoiceEntity.InvoiceStatus.PAID);
            invoice.setYear(2024);

            invoiceJpaRepository.save(invoice);
        }
    }

    /**
     * Limpia todos los datos de prueba.
     */
    public void clearTestData() {
        invoiceJpaRepository.deleteAll();
    }

    /**
     * Obtiene el número de facturas de prueba creadas.
     */
    public long getTestInvoicesCount() {
        return invoiceJpaRepository.count();
    }
}