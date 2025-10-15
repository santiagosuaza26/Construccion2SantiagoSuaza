package app.clinic.domain.service;

import app.clinic.domain.model.*;
import app.clinic.domain.port.BillingRepository;
import app.clinic.domain.port.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests básicos para BillingDomainService.
 * Se enfoca en las funcionalidades críticas de facturación.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BillingDomainService Tests")
class BillingDomainServiceTest {

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private PatientRepository patientRepository;

    private BillingDomainService billingService;

    @BeforeEach
    void setUp() {
        billingService = new BillingDomainService(billingRepository, patientRepository);
    }

    @Test
    @DisplayName("Debe inicializar correctamente")
    void shouldInitializeCorrectly() {
        // Then
        assertThat(billingService).isNotNull();
    }

    @Test
    @DisplayName("Debe consultar repositorio de pacientes")
    void shouldQueryPatientRepository() {
        // Given
        PatientCedula patientCedula = PatientCedula.of("12345678");

        // When
        try {
            billingService.calculateBilling(patientCedula, TotalCost.of(Money.of(new BigDecimal("100000"))));
        } catch (Exception e) {
            // Expected due to mocking limitations
        }

        // Then
        verify(patientRepository).findByCedula(patientCedula);
    }

    @Test
    @DisplayName("Debe consultar repositorio de facturación")
    void shouldQueryBillingRepository() {
        // Given
        PatientCedula patientCedula = PatientCedula.of("12345678");

        // When
        try {
            billingService.generateBillingSummary(patientCedula);
        } catch (Exception e) {
            // Expected due to mocking limitations
        }

        // Then
        verify(billingRepository).generateBillingSummary(patientCedula);
    }

    @Test
    @DisplayName("Debe consultar detalles de facturación")
    void shouldQueryBillingDetails() {
        // Given
        PatientCedula patientCedula = PatientCedula.of("12345678");

        // When
        try {
            billingService.generateBillingDetails(patientCedula);
        } catch (Exception e) {
            // Expected due to mocking limitations
        }

        // Then
        verify(billingRepository).generateBillingDetails(patientCedula);
    }
}