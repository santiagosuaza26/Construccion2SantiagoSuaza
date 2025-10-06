package app.clinic.infrastructure.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import app.clinic.config.TestDatabaseConfig;
import app.clinic.infrastructure.entity.InvoiceEntity;

/**
 * Pruebas de integración para InvoiceJpaRepository.
 * Verifica operaciones CRUD y consultas personalizadas.
 */
@DataJpaTest
@SpringJUnitConfig(TestDatabaseConfig.class)
class InvoiceJpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InvoiceJpaRepository invoiceJpaRepository;

    @Test
    @DisplayName("Debe guardar y recuperar factura correctamente")
    void shouldSaveAndRetrieveInvoiceCorrectly() {
        // Given
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setInvoiceNumber("TEST-2024-001");
        invoice.setPatientCedula("12345678");
        invoice.setTotalAmount(new BigDecimal("200000"));
        invoice.setCopaymentAmount(new BigDecimal("50000"));
        invoice.setInsuranceCoverage(new BigDecimal("150000"));
        invoice.setPatientResponsibility(new BigDecimal("50000"));
        invoice.setBillingDate(LocalDateTime.now());
        invoice.setDueDate(LocalDateTime.now().plusDays(30));
        invoice.setStatus(InvoiceEntity.InvoiceStatus.PENDING);
        invoice.setYear(2024);

        // When
        InvoiceEntity savedInvoice = invoiceJpaRepository.save(invoice);
        InvoiceEntity foundInvoice = invoiceJpaRepository.findByInvoiceNumber("TEST-2024-001");

        // Then
        assertNotNull(savedInvoice.getId());
        assertNotNull(foundInvoice);
        assertEquals("TEST-2024-001", foundInvoice.getInvoiceNumber());
        assertEquals("12345678", foundInvoice.getPatientCedula());
        assertEquals(new BigDecimal("200000"), foundInvoice.getTotalAmount());
        assertEquals(InvoiceEntity.InvoiceStatus.PENDING, foundInvoice.getStatus());
    }

    @Test
    @DisplayName("Debe encontrar facturas por cédula de paciente")
    void shouldFindInvoicesByPatientCedula() {
        // Given
        String patientCedula = "12345678";

        InvoiceEntity invoice1 = createTestInvoice("TEST-2024-001", patientCedula, 2024);
        InvoiceEntity invoice2 = createTestInvoice("TEST-2024-002", patientCedula, 2024);
        InvoiceEntity invoice3 = createTestInvoice("TEST-2024-003", "87654321", 2024);

        invoiceJpaRepository.save(invoice1);
        invoiceJpaRepository.save(invoice2);
        invoiceJpaRepository.save(invoice3);

        // When
        List<InvoiceEntity> foundInvoices = invoiceJpaRepository.findByPatientCedulaOrderByBillingDateDesc(patientCedula);

        // Then
        assertEquals(2, foundInvoices.size());
        assertTrue(foundInvoices.stream().allMatch(inv -> inv.getPatientCedula().equals(patientCedula)));
    }

    @Test
    @DisplayName("Debe encontrar facturas por estado")
    void shouldFindInvoicesByStatus() {
        // Given
        InvoiceEntity pendingInvoice = createTestInvoice("TEST-2024-001", "12345678", 2024);
        pendingInvoice.setStatus(InvoiceEntity.InvoiceStatus.PENDING);

        InvoiceEntity paidInvoice = createTestInvoice("TEST-2024-002", "12345678", 2024);
        paidInvoice.setStatus(InvoiceEntity.InvoiceStatus.PAID);

        invoiceJpaRepository.save(pendingInvoice);
        invoiceJpaRepository.save(paidInvoice);

        // When
        List<InvoiceEntity> pendingInvoices = invoiceJpaRepository.findByStatusOrderByBillingDateDesc(InvoiceEntity.InvoiceStatus.PENDING);
        List<InvoiceEntity> paidInvoices = invoiceJpaRepository.findByStatusOrderByBillingDateDesc(InvoiceEntity.InvoiceStatus.PAID);

        // Then
        assertEquals(1, pendingInvoices.size());
        assertEquals(1, paidInvoices.size());
        assertEquals(InvoiceEntity.InvoiceStatus.PENDING, pendingInvoices.get(0).getStatus());
        assertEquals(InvoiceEntity.InvoiceStatus.PAID, paidInvoices.get(0).getStatus());
    }

    @Test
    @DisplayName("Debe calcular total de copagos por año correctamente")
    void shouldCalculateTotalCopaymentForYearCorrectly() {
        // Given
        String patientCedula = "12345678";
        Integer year = 2024;

        InvoiceEntity invoice1 = createTestInvoice("TEST-2024-001", patientCedula, year);
        invoice1.setCopaymentAmount(new BigDecimal("50000"));
        invoice1.setStatus(InvoiceEntity.InvoiceStatus.PAID);

        InvoiceEntity invoice2 = createTestInvoice("TEST-2024-002", patientCedula, year);
        invoice2.setCopaymentAmount(new BigDecimal("30000"));
        invoice2.setStatus(InvoiceEntity.InvoiceStatus.PAID);

        InvoiceEntity invoice3 = createTestInvoice("TEST-2024-003", patientCedula, year);
        invoice3.setCopaymentAmount(new BigDecimal("20000"));
        invoice3.setStatus(InvoiceEntity.InvoiceStatus.CANCELLED); // Esta no debe contarse

        invoiceJpaRepository.save(invoice1);
        invoiceJpaRepository.save(invoice2);
        invoiceJpaRepository.save(invoice3);

        // When
        BigDecimal totalCopayment = invoiceJpaRepository.getTotalCopaymentForYear(patientCedula, year);

        // Then
        assertNotNull(totalCopayment);
        assertEquals(BigDecimal.ZERO, totalCopayment); // Las facturas marcadas como CANCELLED no se cuentan
    }

    @Test
    @DisplayName("Debe calcular total facturado por año correctamente")
    void shouldCalculateTotalBilledForYearCorrectly() {
        // Given
        String patientCedula = "12345678";
        Integer year = 2024;

        InvoiceEntity invoice1 = createTestInvoice("TEST-2024-001", patientCedula, year);
        invoice1.setTotalAmount(new BigDecimal("200000"));
        invoice1.setStatus(InvoiceEntity.InvoiceStatus.PAID);

        InvoiceEntity invoice2 = createTestInvoice("TEST-2024-002", patientCedula, year);
        invoice2.setTotalAmount(new BigDecimal("150000"));
        invoice2.setStatus(InvoiceEntity.InvoiceStatus.PAID);

        invoiceJpaRepository.save(invoice1);
        invoiceJpaRepository.save(invoice2);

        // When
        BigDecimal totalBilled = invoiceJpaRepository.getTotalBilledForYear(patientCedula, year);

        // Then
        assertNotNull(totalBilled);
        assertEquals(new BigDecimal("350000"), totalBilled); // 200000 + 150000 (ambas están marcadas como PAID)
    }

    @Test
    @DisplayName("Debe verificar existencia de factura por número")
    void shouldCheckInvoiceExistenceByNumber() {
        // Given
        InvoiceEntity invoice = createTestInvoice("TEST-2024-001", "12345678", 2024);
        invoiceJpaRepository.save(invoice);

        // When & Then
        assertTrue(invoiceJpaRepository.existsByInvoiceNumber("TEST-2024-001"));
        assertFalse(invoiceJpaRepository.existsByInvoiceNumber("NON-EXISTENT"));
    }

    @Test
    @DisplayName("Debe contar facturas por paciente correctamente")
    void shouldCountInvoicesByPatientCorrectly() {
        // Given
        String patientCedula = "12345678";

        InvoiceEntity invoice1 = createTestInvoice("TEST-2024-001", patientCedula, 2024);
        InvoiceEntity invoice2 = createTestInvoice("TEST-2024-002", patientCedula, 2024);
        InvoiceEntity invoice3 = createTestInvoice("TEST-2024-003", "87654321", 2024);

        invoiceJpaRepository.save(invoice1);
        invoiceJpaRepository.save(invoice2);
        invoiceJpaRepository.save(invoice3);

        // When
        long count = invoiceJpaRepository.countByPatientCedula(patientCedula);

        // Then
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay facturas")
    void shouldReturnEmptyListWhenNoInvoices() {
        // When
        List<InvoiceEntity> allInvoices = invoiceJpaRepository.findAll();

        // Then
        assertTrue(allInvoices.isEmpty());
    }

    /**
     * Método auxiliar para crear facturas de prueba.
     */
    private InvoiceEntity createTestInvoice(String invoiceNumber, String patientCedula, Integer year) {
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setPatientCedula(patientCedula);
        invoice.setTotalAmount(new BigDecimal("100000"));
        invoice.setCopaymentAmount(new BigDecimal("25000"));
        invoice.setInsuranceCoverage(new BigDecimal("75000"));
        invoice.setPatientResponsibility(new BigDecimal("25000"));
        invoice.setBillingDate(LocalDateTime.now());
        invoice.setDueDate(LocalDateTime.now().plusDays(30));
        invoice.setStatus(InvoiceEntity.InvoiceStatus.PENDING);
        invoice.setYear(year);
        return invoice;
    }
}