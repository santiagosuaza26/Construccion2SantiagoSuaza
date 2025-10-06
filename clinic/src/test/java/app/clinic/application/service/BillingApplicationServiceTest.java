package app.clinic.application.service;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import org.mockito.MockitoAnnotations;

import app.clinic.application.dto.billing.BillingCalculationResultDTO;
import app.clinic.application.dto.billing.InvoiceDTO;
import app.clinic.application.dto.patient.PatientDTO;
import app.clinic.application.mapper.BillingMapper;
import app.clinic.domain.service.BillingDomainService;
import app.clinic.infrastructure.adapter.BillingRepositoryAdapter;
import app.clinic.infrastructure.repository.InvoiceJpaRepository;

/**
 * Pruebas unitarias para BillingApplicationService.
 * Verifica la lógica de negocio de facturación.
 */
class BillingApplicationServiceTest {

    @Mock
    private BillingDomainService billingDomainService;

    @Mock
    private PatientApplicationService patientApplicationService;

    @Mock
    private BillingRepositoryAdapter billingRepositoryAdapter;

    @Mock
    private BillingMapper billingMapper;

    @Mock
    private InvoiceJpaRepository invoiceJpaRepository;

    private BillingApplicationService billingApplicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        billingApplicationService = new BillingApplicationService(
            billingDomainService,
            patientApplicationService,
            billingRepositoryAdapter,
            billingMapper,
            invoiceJpaRepository
        );
    }

    @Test
    @DisplayName("Debe calcular facturación correctamente para paciente con seguro activo")
    void shouldCalculateBillingCorrectlyForPatientWithActiveInsurance() {
        // Given
        String patientCedula = "12345678";

        // When
        BillingCalculationResultDTO result = billingApplicationService.calculateBilling(patientCedula);

        // Then
        assertNotNull(result);
        assertNotNull(result.getTotalCost());
        assertNotNull(result.getCopaymentAmount());
        assertNotNull(result.getInsuranceCoverageAmount());
        assertNotNull(result.getCopaymentLimitMessage());

        // Verificar que el copago estándar se aplique correctamente
        assertEquals("50000", result.getCopaymentAmount());
        assertTrue(result.isRequiresCopayment());
        assertFalse(result.isCopaymentLimitExceeded());
    }

    @Test
    @DisplayName("Debe generar factura correctamente")
    void shouldGenerateInvoiceCorrectly() {
        // Given
        String patientCedula = "12345678";

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFullName("María González");
        doReturn(Optional.of(patientDTO)).when(patientApplicationService).findPatientByCedula(patientCedula);

        // When
        InvoiceDTO invoice = billingApplicationService.generateInvoice(patientCedula);

        // Then
        assertNotNull(invoice);
        assertNotNull(invoice.getInvoiceNumber());
        assertEquals(patientCedula, invoice.getPatientCedula());
        assertEquals("María González", invoice.getPatientName());
        assertNotNull(invoice.getTotalAmount());
        assertNotNull(invoice.getCopaymentAmount());
        assertNotNull(invoice.getInsuranceCoverage());
        assertNotNull(invoice.getPatientResponsibility());
        assertNotNull(invoice.getBillingDate());
        assertNotNull(invoice.getDueDate());
        assertNotNull(invoice.getStatus());
        assertNotNull(invoice.getYear());

        // Verificar valores específicos
        assertEquals("200000", invoice.getTotalAmount());
        assertEquals("50000", invoice.getCopaymentAmount());
        assertEquals("150000", invoice.getInsuranceCoverage());
        assertEquals("50000", invoice.getPatientResponsibility());
        assertEquals("PENDING", invoice.getStatus());
    }

    @Test
    @DisplayName("Debe calcular monto de copago correctamente")
    void shouldCalculateCopaymentAmountCorrectly() {
        // Given
        String patientCedula = "12345678";
        BigDecimal totalAmount = new BigDecimal("200000");

        // When
        BigDecimal copayment = billingApplicationService.calculateCopaymentAmount(patientCedula, totalAmount);

        // Then
        assertNotNull(copayment);
        assertEquals(new BigDecimal("50000"), copayment);
    }

    @Test
    @DisplayName("Debe validar póliza de seguro correctamente")
    void shouldValidateInsurancePolicyCorrectly() {
        // Given
        String patientCedula = "12345678";

        // When
        boolean isValid = billingApplicationService.validateInsurancePolicyForBilling(patientCedula);

        // Then
        assertTrue(isValid); // Actualmente siempre retorna true según la implementación
    }

    @Test
    @DisplayName("Debe verificar límite anual de copagos correctamente")
    void shouldCheckAnnualCopaymentLimitCorrectly() {
        // Given
        String patientCedula = "12345678";

        // When
        boolean exceeded = billingApplicationService.hasExceededAnnualCopaymentLimit(patientCedula);

        // Then
        assertFalse(exceeded); // Actualmente siempre retorna false según la implementación
    }

    @Test
    @DisplayName("Debe obtener copago acumulado por año correctamente")
    void shouldGetAccumulatedCopaymentForYearCorrectly() {
        // Given
        String patientCedula = "12345678";
        int year = 2024;

        // When
        BigDecimal accumulated = billingApplicationService.getAccumulatedCopaymentForYear(patientCedula, year);

        // Then
        assertNotNull(accumulated);
        assertEquals(BigDecimal.ZERO, accumulated); // Actualmente siempre retorna cero
    }

    @Test
    @DisplayName("Debe aplicar reglas de negocio de facturación correctamente")
    void shouldApplyBillingBusinessRulesCorrectly() {
        // Given
        String patientCedula = "12345678";
        BigDecimal totalAmount = new BigDecimal("200000");
        boolean hasActiveInsurance = true;

        // When
        BillingCalculationResultDTO result = billingApplicationService
            .applyBillingBusinessRules(patientCedula, totalAmount, hasActiveInsurance);

        // Then
        assertNotNull(result);
        assertEquals("200000", result.getTotalCost());
        assertEquals("50000", result.getCopaymentAmount());
        assertEquals("150000", result.getInsuranceCoverageAmount());
        assertTrue(result.isRequiresCopayment());
        assertFalse(result.isCopaymentLimitExceeded());
        assertNotNull(result.getCopaymentLimitMessage());
    }

    @Test
    @DisplayName("Debe manejar caso sin seguro activo correctamente")
    void shouldHandleCaseWithoutActiveInsuranceCorrectly() {
        // Given
        String patientCedula = "12345678";
        BigDecimal totalAmount = new BigDecimal("200000");
        boolean hasActiveInsurance = false;

        // When
        BillingCalculationResultDTO result = billingApplicationService
            .applyBillingBusinessRules(patientCedula, totalAmount, hasActiveInsurance);

        // Then
        assertNotNull(result);
        assertEquals("200000", result.getTotalCost());
        assertEquals("200000", result.getCopaymentAmount()); // Debe pagar todo
        assertEquals("0", result.getInsuranceCoverageAmount()); // Sin cobertura
        assertTrue(result.isRequiresCopayment());
        assertFalse(result.isCopaymentLimitExceeded());
        assertTrue(result.getCopaymentLimitMessage().contains("Sin póliza activa"));
    }

    @Test
    @DisplayName("Debe manejar caso con límite de copago excedido correctamente")
    void shouldHandleCaseWithExceededCopaymentLimitCorrectly() {
        // Given
        String patientCedula = "12345678";
        BigDecimal totalAmount = new BigDecimal("200000");
        boolean hasActiveInsurance = true;

        // Simular que el paciente excedió el límite anual
        billingApplicationService = spy(billingApplicationService);
        doReturn(true).when(billingApplicationService).hasExceededAnnualCopaymentLimit(patientCedula);

        // When
        BillingCalculationResultDTO result = billingApplicationService
            .applyBillingBusinessRules(patientCedula, totalAmount, hasActiveInsurance);

        // Then
        assertNotNull(result);
        assertEquals("200000", result.getTotalCost());
        assertEquals("0", result.getCopaymentAmount()); // No paga copago
        assertEquals("200000", result.getInsuranceCoverageAmount()); // Seguro cubre todo
        assertFalse(result.isRequiresCopayment());
        assertTrue(result.isCopaymentLimitExceeded());
        assertTrue(result.getCopaymentLimitMessage().contains("excedido el límite anual"));
    }

}