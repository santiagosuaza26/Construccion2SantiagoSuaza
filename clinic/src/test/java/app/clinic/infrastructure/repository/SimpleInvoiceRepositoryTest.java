package app.clinic.infrastructure.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import app.clinic.infrastructure.entity.InvoiceEntity;

/**
 * Prueba simplificada para verificar creación básica de tablas y operaciones CRUD.
 */
@DataJpaTest
class SimpleInvoiceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InvoiceJpaRepository invoiceJpaRepository;

    @Test
    @DisplayName("Debe crear tablas correctamente y permitir operaciones básicas")
    void shouldCreateTablesCorrectlyAndAllowBasicOperations() {
        // Given
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setInvoiceNumber("SIMPLE-TEST-001");
        invoice.setPatientCedula("12345678");
        invoice.setTotalAmount(new BigDecimal("100000"));
        invoice.setCopaymentAmount(new BigDecimal("25000"));
        invoice.setInsuranceCoverage(new BigDecimal("75000"));
        invoice.setPatientResponsibility(new BigDecimal("25000"));
        invoice.setBillingDate(LocalDateTime.now());
        invoice.setDueDate(LocalDateTime.now().plusDays(30));
        invoice.setStatus(InvoiceEntity.InvoiceStatus.PENDING);
        invoice.setYear(2024);

        // When
        InvoiceEntity savedInvoice = entityManager.persistAndFlush(invoice);

        // Then
        assertNotNull(savedInvoice.getId());
        assertEquals("SIMPLE-TEST-001", savedInvoice.getInvoiceNumber());
        assertEquals("12345678", savedInvoice.getPatientCedula());
        assertEquals(new BigDecimal("100000"), savedInvoice.getTotalAmount());
        assertEquals(InvoiceEntity.InvoiceStatus.PENDING, savedInvoice.getStatus());
    }

    @Test
    @DisplayName("Debe permitir consultas básicas")
    void shouldAllowBasicQueries() {
        // Given
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setInvoiceNumber("QUERY-TEST-001");
        invoice.setPatientCedula("87654321");
        invoice.setTotalAmount(new BigDecimal("150000"));
        invoice.setStatus(InvoiceEntity.InvoiceStatus.PAID);
        invoice.setYear(2024);

        entityManager.persistAndFlush(invoice);

        // When
        InvoiceEntity found = invoiceJpaRepository.findByInvoiceNumber("QUERY-TEST-001");

        // Then
        assertNotNull(found);
        assertEquals("QUERY-TEST-001", found.getInvoiceNumber());
        assertEquals("87654321", found.getPatientCedula());
        assertEquals(InvoiceEntity.InvoiceStatus.PAID, found.getStatus());
    }

    @Test
    @DisplayName("Debe manejar operaciones de conteo")
    void shouldHandleCountOperations() {
        // Given - Crear algunas facturas
        for (int i = 1; i <= 3; i++) {
            InvoiceEntity invoice = new InvoiceEntity();
            invoice.setInvoiceNumber("COUNT-TEST-00" + i);
            invoice.setPatientCedula("11111111");
            invoice.setTotalAmount(new BigDecimal("100000"));
            invoice.setStatus(InvoiceEntity.InvoiceStatus.PENDING);
            invoice.setYear(2024);
            entityManager.persist(invoice);
        }
        entityManager.flush();

        // When
        long count = invoiceJpaRepository.count();

        // Then
        assertTrue(count >= 3);
    }
}