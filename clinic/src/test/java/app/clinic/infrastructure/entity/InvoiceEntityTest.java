package app.clinic.infrastructure.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la entidad InvoiceEntity.
 * Verifica el comportamiento correcto de la entidad JPA.
 */
class InvoiceEntityTest {

    @Test
    @DisplayName("Debe crear InvoiceEntity correctamente con constructor completo")
    void shouldCreateInvoiceEntityWithFullConstructor() {
        // Given
        String invoiceNumber = "INV-2024-001";
        String patientCedula = "12345678";
        BigDecimal totalAmount = new BigDecimal("200000");
        BigDecimal copaymentAmount = new BigDecimal("50000");
        BigDecimal insuranceCoverage = new BigDecimal("150000");
        BigDecimal patientResponsibility = new BigDecimal("50000");
        LocalDateTime billingDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.now().plusDays(30);
        InvoiceEntity.InvoiceStatus status = InvoiceEntity.InvoiceStatus.PENDING;
        String notes = "Factura de consulta médica";
        Integer year = 2024;

        // When
        InvoiceEntity invoice = new InvoiceEntity(invoiceNumber, patientCedula, totalAmount,
                                                copaymentAmount, insuranceCoverage, patientResponsibility,
                                                billingDate, dueDate, status, notes, year);

        // Then
        assertNotNull(invoice);
        assertEquals(invoiceNumber, invoice.getInvoiceNumber());
        assertEquals(patientCedula, invoice.getPatientCedula());
        assertEquals(totalAmount, invoice.getTotalAmount());
        assertEquals(copaymentAmount, invoice.getCopaymentAmount());
        assertEquals(insuranceCoverage, invoice.getInsuranceCoverage());
        assertEquals(patientResponsibility, invoice.getPatientResponsibility());
        assertEquals(billingDate, invoice.getBillingDate());
        assertEquals(dueDate, invoice.getDueDate());
        assertEquals(status, invoice.getStatus());
        assertEquals(notes, invoice.getNotes());
        assertEquals(year, invoice.getYear());
    }

    @Test
    @DisplayName("Debe crear InvoiceEntity correctamente con constructor vacío")
    void shouldCreateInvoiceEntityWithEmptyConstructor() {
        // When
        InvoiceEntity invoice = new InvoiceEntity();

        // Then
        assertNotNull(invoice);
        assertNull(invoice.getId());
        assertNull(invoice.getInvoiceNumber());
        assertNull(invoice.getPatientCedula());
        assertNull(invoice.getTotalAmount());
        assertNull(invoice.getStatus());
    }

    @Test
    @DisplayName("Debe establecer y obtener ID correctamente")
    void shouldSetAndGetIdCorrectly() {
        // Given
        InvoiceEntity invoice = new InvoiceEntity();
        Long expectedId = 1L;

        // When
        invoice.setId(expectedId);

        // Then
        assertEquals(expectedId, invoice.getId());
    }

    @Test
    @DisplayName("Debe generar representación en cadena correcta")
    void shouldGenerateCorrectStringRepresentation() {
        // Given
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId(1L);
        invoice.setInvoiceNumber("INV-2024-001");
        invoice.setPatientCedula("12345678");
        invoice.setTotalAmount(new BigDecimal("200000"));
        invoice.setStatus(InvoiceEntity.InvoiceStatus.PENDING);

        // When
        String toString = invoice.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("InvoiceEntity"));
        assertTrue(toString.contains("INV-2024-001"));
        assertTrue(toString.contains("12345678"));
        assertTrue(toString.contains("200000"));
        assertTrue(toString.contains("PENDING"));
    }

    @Test
    @DisplayName("Debe manejar valores nulos correctamente")
    void shouldHandleNullValuesCorrectly() {
        // Given
        InvoiceEntity invoice = new InvoiceEntity();

        // When & Then
        assertDoesNotThrow(() -> {
            invoice.setInvoiceNumber(null);
            invoice.setPatientCedula(null);
            invoice.setTotalAmount(null);
            invoice.setStatus(null);
            invoice.setNotes(null);
            invoice.setYear(null);
        });

        assertNull(invoice.getInvoiceNumber());
        assertNull(invoice.getPatientCedula());
        assertNull(invoice.getTotalAmount());
        assertNull(invoice.getStatus());
        assertNull(invoice.getNotes());
        assertNull(invoice.getYear());
    }
}