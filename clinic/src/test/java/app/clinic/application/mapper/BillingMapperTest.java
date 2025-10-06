package app.clinic.application.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import app.clinic.application.dto.billing.BillingCalculationResultDTO;
import app.clinic.application.dto.billing.BillingDTO;
import app.clinic.application.dto.billing.InvoiceDTO;
import app.clinic.application.dto.patient.PatientDTO;
import app.clinic.application.service.PatientApplicationService;
import app.clinic.infrastructure.entity.BillingSummaryEntity;
import app.clinic.infrastructure.entity.InvoiceEntity;

/**
 * Pruebas unitarias para BillingMapper.
 * Verifica las conversiones entre entidades y DTOs.
 */
@ExtendWith(MockitoExtension.class)
class BillingMapperTest {

    @Mock
    private PatientApplicationService patientApplicationService;

    private BillingMapper billingMapper;

    @BeforeEach
    void setUp() {
        billingMapper = new BillingMapper(patientApplicationService);
    }

    @Test
    @DisplayName("Debe convertir InvoiceEntity a InvoiceDTO correctamente")
    void shouldConvertInvoiceEntityToInvoiceDTOCorrectly() {
        // Given
        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(1L);
        entity.setInvoiceNumber("INV-2024-001");
        entity.setPatientCedula("12345678");
        entity.setTotalAmount(new BigDecimal("200000"));
        entity.setCopaymentAmount(new BigDecimal("50000"));
        entity.setInsuranceCoverage(new BigDecimal("150000"));
        entity.setPatientResponsibility(new BigDecimal("50000"));
        entity.setBillingDate(LocalDateTime.of(2024, 10, 1, 10, 0));
        entity.setDueDate(LocalDateTime.of(2024, 11, 1, 10, 0));
        entity.setStatus(InvoiceEntity.InvoiceStatus.PENDING);
        entity.setNotes("Factura de consulta médica");
        entity.setYear(2024);

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFullName("María González");
        when(patientApplicationService.findPatientByCedula("12345678"))
            .thenReturn(Optional.of(patientDTO));

        // When
        InvoiceDTO dto = billingMapper.toInvoiceDTO(entity);

        // Then
        assertNotNull(dto);
        assertEquals("INV-2024-001", dto.getInvoiceNumber());
        assertEquals("12345678", dto.getPatientCedula());
        assertEquals("María González", dto.getPatientName());
        assertEquals("200000", dto.getTotalAmount());
        assertEquals("50000", dto.getCopaymentAmount());
        assertEquals("150000", dto.getInsuranceCoverage());
        assertEquals("50000", dto.getPatientResponsibility());
        assertNotNull(dto.getBillingDate());
        assertNotNull(dto.getDueDate());
        assertEquals("PENDING", dto.getStatus());
        assertEquals("Factura de consulta médica", dto.getNotes());
        assertEquals(Integer.valueOf(2024), dto.getYear());
    }

    @Test
    @DisplayName("Debe manejar InvoiceEntity nula correctamente")
    void shouldHandleNullInvoiceEntityCorrectly() {
        // When
        InvoiceDTO dto = billingMapper.toInvoiceDTO(null);

        // Then
        assertNull(dto);
    }

    @Test
    @DisplayName("Debe convertir lista de InvoiceEntity a lista de InvoiceDTO correctamente")
    void shouldConvertInvoiceEntityListToInvoiceDTOListCorrectly() {
        // Given
        InvoiceEntity entity1 = new InvoiceEntity();
        entity1.setInvoiceNumber("INV-2024-001");
        entity1.setPatientCedula("12345678");
        entity1.setTotalAmount(new BigDecimal("200000"));

        InvoiceEntity entity2 = new InvoiceEntity();
        entity2.setInvoiceNumber("INV-2024-002");
        entity2.setPatientCedula("87654321");
        entity2.setTotalAmount(new BigDecimal("150000"));

        List<InvoiceEntity> entities = List.of(entity1, entity2);

        // When
        List<InvoiceDTO> dtos = billingMapper.toInvoiceDTOList(entities);

        // Then
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("INV-2024-001", dtos.get(0).getInvoiceNumber());
        assertEquals("INV-2024-002", dtos.get(1).getInvoiceNumber());
    }

    @Test
    @DisplayName("Debe manejar lista nula correctamente")
    void shouldHandleNullListCorrectly() {
        // When
        List<InvoiceDTO> dtos = billingMapper.toInvoiceDTOList(null);

        // Then
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    @DisplayName("Debe crear BillingCalculationResultDTO correctamente")
    void shouldCreateBillingCalculationResultDTOCorrectly() {
        // Given
        BigDecimal totalCost = new BigDecimal("200000");
        BigDecimal copaymentAmount = new BigDecimal("50000");
        BigDecimal insuranceCoverage = new BigDecimal("150000");
        boolean requiresCopayment = true;
        boolean copaymentLimitExceeded = false;

        // When
        BillingCalculationResultDTO dto = billingMapper.toBillingCalculationResultDTO(
            totalCost, copaymentAmount, insuranceCoverage, requiresCopayment, copaymentLimitExceeded);

        // Then
        assertNotNull(dto);
        assertEquals("200000", dto.getTotalCost());
        assertEquals("50000", dto.getCopaymentAmount());
        assertEquals("150000", dto.getInsuranceCoverageAmount());
        assertTrue(dto.isRequiresCopayment());
        assertFalse(dto.isCopaymentLimitExceeded());
        assertNotNull(dto.getCopaymentLimitMessage());
        assertTrue(dto.getCopaymentLimitMessage().contains("Paciente debe pagar copago"));
    }

    @Test
    @DisplayName("Debe manejar caso con límite de copago excedido correctamente")
    void shouldHandleExceededCopaymentLimitCaseCorrectly() {
        // Given
        BigDecimal totalCost = new BigDecimal("200000");
        BigDecimal copaymentAmount = new BigDecimal("0");
        BigDecimal insuranceCoverage = new BigDecimal("200000");
        boolean requiresCopayment = false;
        boolean copaymentLimitExceeded = true;

        // When
        BillingCalculationResultDTO dto = billingMapper.toBillingCalculationResultDTO(
            totalCost, copaymentAmount, insuranceCoverage, requiresCopayment, copaymentLimitExceeded);

        // Then
        assertNotNull(dto);
        assertFalse(dto.isRequiresCopayment());
        assertTrue(dto.isCopaymentLimitExceeded());
        assertTrue(dto.getCopaymentLimitMessage().contains("excedido el límite anual"));
    }

    @Test
    @DisplayName("Debe manejar valores nulos en montos correctamente")
    void shouldHandleNullAmountValuesCorrectly() {
        // Given
        InvoiceEntity entity = new InvoiceEntity();
        entity.setInvoiceNumber("INV-2024-001");
        entity.setPatientCedula("12345678");
        // Los montos son nulos

        // When
        InvoiceDTO dto = billingMapper.toInvoiceDTO(entity);

        // Then
        assertNotNull(dto);
        assertEquals("INV-2024-001", dto.getInvoiceNumber());
        assertEquals("12345678", dto.getPatientCedula());
        assertEquals("0", dto.getTotalAmount()); // Debe convertir null a "0"
        assertEquals("0", dto.getCopaymentAmount());
        assertEquals("0", dto.getInsuranceCoverage());
        assertEquals("0", dto.getPatientResponsibility());
    }

    @Test
    @DisplayName("Debe formatear fechas correctamente")
    void shouldFormatDatesCorrectly() {
        // Given
        InvoiceEntity entity = new InvoiceEntity();
        entity.setInvoiceNumber("INV-2024-001");
        entity.setPatientCedula("12345678");
        entity.setBillingDate(LocalDateTime.of(2024, 10, 1, 14, 30, 0));
        entity.setDueDate(LocalDateTime.of(2024, 11, 1, 14, 30, 0));

        // When
        InvoiceDTO dto = billingMapper.toInvoiceDTO(entity);

        // Then
        assertNotNull(dto);
        assertNotNull(dto.getBillingDate());
        assertNotNull(dto.getDueDate());
        // Las fechas deben estar en formato ISO
        assertTrue(dto.getBillingDate().contains("2024-10-01"));
        assertTrue(dto.getDueDate().contains("2024-11-01"));
    }

    @Test
    @DisplayName("Debe convertir BillingSummaryEntity a BillingDTO con nombre real del paciente")
    void shouldConvertBillingSummaryEntityToBillingDTOWithRealPatientName() {
        // Given
        BillingSummaryEntity entity = new BillingSummaryEntity();
        entity.setPatientCedula("12345678");
        entity.setTotalBilled(new BigDecimal("200000"));
        entity.setTotalCopayment(new BigDecimal("50000"));
        entity.setTotalInsuranceCoverage(new BigDecimal("150000"));

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFullName("María González");
        when(patientApplicationService.findPatientByCedula("12345678"))
            .thenReturn(Optional.of(patientDTO));

        // When
        BillingDTO dto = billingMapper.toBillingDTO(entity);

        // Then
        assertNotNull(dto);
        assertEquals("12345678", dto.getPatientCedula());
        assertEquals("María González", dto.getPatientName());
        assertEquals("200000", dto.getTotalAmount());
        assertEquals("50000", dto.getCopaymentAmount());
        assertEquals("150000", dto.getInsuranceCoverageAmount());
    }

    @Test
    @DisplayName("Debe manejar paciente no encontrado correctamente")
    void shouldHandlePatientNotFoundCorrectly() {
        // Given
        BillingSummaryEntity entity = new BillingSummaryEntity();
        entity.setPatientCedula("99999999");
        entity.setTotalBilled(new BigDecimal("100000"));

        when(patientApplicationService.findPatientByCedula("99999999"))
            .thenReturn(Optional.empty());

        // When
        BillingDTO dto = billingMapper.toBillingDTO(entity);

        // Then
        assertNotNull(dto);
        assertEquals("99999999", dto.getPatientCedula());
        assertEquals("Paciente no encontrado", dto.getPatientName());
        assertEquals("100000", dto.getTotalAmount());
    }

    @Test
    @DisplayName("Debe convertir InvoiceEntity a InvoiceDTO con nombre real del paciente")
    void shouldConvertInvoiceEntityToInvoiceDTOWithRealPatientName() {
        // Given
        InvoiceEntity entity = new InvoiceEntity();
        entity.setInvoiceNumber("INV-2024-001");
        entity.setPatientCedula("12345678");
        entity.setTotalAmount(new BigDecimal("200000"));

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFullName("Carlos Rodríguez");
        when(patientApplicationService.findPatientByCedula("12345678"))
            .thenReturn(Optional.of(patientDTO));

        // When
        InvoiceDTO dto = billingMapper.toInvoiceDTO(entity);

        // Then
        assertNotNull(dto);
        assertEquals("INV-2024-001", dto.getInvoiceNumber());
        assertEquals("12345678", dto.getPatientCedula());
        assertEquals("Carlos Rodríguez", dto.getPatientName());
        assertEquals("200000", dto.getTotalAmount());
    }

    @Test
    @DisplayName("Debe manejar paciente no encontrado en toInvoiceDTO correctamente")
    void shouldHandlePatientNotFoundInToInvoiceDTOCorrectly() {
        // Given
        InvoiceEntity entity = new InvoiceEntity();
        entity.setInvoiceNumber("INV-2024-001");
        entity.setPatientCedula("99999999");
        entity.setTotalAmount(new BigDecimal("100000"));

        when(patientApplicationService.findPatientByCedula("99999999"))
            .thenReturn(Optional.empty());

        // When
        InvoiceDTO dto = billingMapper.toInvoiceDTO(entity);

        // Then
        assertNotNull(dto);
        assertEquals("INV-2024-001", dto.getInvoiceNumber());
        assertEquals("99999999", dto.getPatientCedula());
        assertEquals("Paciente no encontrado", dto.getPatientName());
        assertEquals("100000", dto.getTotalAmount());
    }
}