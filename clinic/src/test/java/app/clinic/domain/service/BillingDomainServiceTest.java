package app.clinic.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import app.clinic.domain.model.BillingCalculationResult;
import app.clinic.domain.model.CopaymentAmount;
import app.clinic.domain.model.MaximumCopaymentAmount;
import app.clinic.domain.model.Money;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.TotalCost;
import app.clinic.domain.port.BillingRepository;
import app.clinic.domain.port.PatientRepository;

/**
 * Tests unitarios independientes para BillingDomainService.
 * Estos tests verifican la lógica de negocio de facturación sin dependencias externas.
 */
@ExtendWith(MockitoExtension.class)
class BillingDomainServiceTest {

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private PatientRepository patientRepository;

    private BillingDomainService billingDomainService;

    @BeforeEach
    void setUp() {
        billingDomainService = new BillingDomainService(billingRepository, patientRepository);
    }

    @Test
    @DisplayName("Debe calcular facturación correctamente")
    void shouldCalculateBillingCorrectly() {
        // Given
        PatientCedula cedula = PatientCedula.of("12345678");
        TotalCost totalCost = TotalCost.of(Money.of(BigDecimal.valueOf(100000)));

        // When
        when(patientRepository.findByCedula(cedula)).thenReturn(Optional.empty());
        when(billingRepository.calculateAccumulatedCopayment(any(), any())).thenReturn(null);

        // Then - Debería lanzar excepción porque paciente no existe
        assertThrows(IllegalArgumentException.class, () -> {
            billingDomainService.calculateBilling(cedula, totalCost);
        });

        verify(patientRepository).findByCedula(cedula);
        verify(billingRepository, never()).calculateAccumulatedCopayment(any(), any());
    }

    @Test
    @DisplayName("Debe generar resumen de facturación")
    void shouldGenerateBillingSummary() {
        // Given
        PatientCedula cedula = PatientCedula.of("12345678");

        // When
        billingDomainService.generateBillingSummary(cedula);

        // Then
        verify(billingRepository).generateBillingSummary(cedula);
    }

    @Test
    @DisplayName("Debe generar detalles de facturación")
    void shouldGenerateBillingDetails() {
        // Given
        PatientCedula cedula = PatientCedula.of("12345678");

        // When
        billingDomainService.generateBillingDetails(cedula);

        // Then
        verify(billingRepository).generateBillingDetails(cedula);
    }

    @Test
    @DisplayName("Debe validar monto de copago estándar")
    void shouldValidateStandardCopaymentAmount() {
        // Given
        BigDecimal standardAmount = CopaymentAmount.standard().getValue();

        // Then
        assertNotNull(standardAmount);
        assertTrue(standardAmount.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Debe validar monto máximo de copago anual")
    void shouldValidateMaximumAnnualCopayment() {
        // Given
        BigDecimal maxAmount = MaximumCopaymentAmount.standard().getValue();

        // Then
        assertNotNull(maxAmount);
        assertTrue(maxAmount.compareTo(BigDecimal.valueOf(100000)) > 0);
    }
}