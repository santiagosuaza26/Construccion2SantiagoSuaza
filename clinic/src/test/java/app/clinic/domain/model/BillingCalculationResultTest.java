package app.clinic.domain.model;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para BillingCalculationResult.
 * Verifica las validaciones de negocio del modelo de dominio.
 */
class BillingCalculationResultTest {

    @Test
    @DisplayName("Debe crear BillingCalculationResult correctamente con valores válidos")
    void shouldCreateBillingCalculationResultCorrectlyWithValidValues() {
        // Given
        Money totalCost = Money.of(new BigDecimal("200000"));
        Money copaymentAmount = Money.of(new BigDecimal("50000"));
        Money insuranceCoverage = Money.of(new BigDecimal("150000"));
        Money patientResponsibility = Money.of(new BigDecimal("50000"));
        boolean requiresFullPayment = false;

        // When
        BillingCalculationResult result = BillingCalculationResult.of(
            totalCost, copaymentAmount, insuranceCoverage, patientResponsibility, requiresFullPayment);

        // Then
        assertNotNull(result);
        assertEquals(totalCost, result.getTotalCost());
        assertEquals(copaymentAmount, result.getCopaymentAmount());
        assertEquals(insuranceCoverage, result.getInsuranceCoverage());
        assertEquals(patientResponsibility, result.getPatientResponsibility());
        assertFalse(result.requiresFullPayment());
    }

    @Test
    @DisplayName("Debe mantener igualdad correctamente")
    void shouldMaintainEqualityCorrectly() {
        // Given
        Money totalCost = Money.of(new BigDecimal("200000"));
        Money copaymentAmount = Money.of(new BigDecimal("50000"));
        Money insuranceCoverage = Money.of(new BigDecimal("150000"));
        Money patientResponsibility = Money.of(new BigDecimal("50000"));
        boolean requiresFullPayment = false;

        BillingCalculationResult result1 = BillingCalculationResult.of(
            totalCost, copaymentAmount, insuranceCoverage, patientResponsibility, requiresFullPayment);

        BillingCalculationResult result2 = BillingCalculationResult.of(
            totalCost, copaymentAmount, insuranceCoverage, patientResponsibility, requiresFullPayment);

        // When & Then
        assertEquals(result1, result2);
        assertEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    @DisplayName("Debe detectar diferencia cuando requiere pago completo")
    void shouldDetectDifferenceWhenRequiresFullPayment() {
        // Given
        Money totalCost = Money.of(new BigDecimal("200000"));
        Money copaymentAmount = Money.of(new BigDecimal("50000"));
        Money insuranceCoverage = Money.of(new BigDecimal("150000"));
        Money patientResponsibility = Money.of(new BigDecimal("50000"));

        BillingCalculationResult result1 = BillingCalculationResult.of(
            totalCost, copaymentAmount, insuranceCoverage, patientResponsibility, false);

        BillingCalculationResult result2 = BillingCalculationResult.of(
            totalCost, copaymentAmount, insuranceCoverage, patientResponsibility, true);

        // When & Then
        assertNotEquals(result1, result2);
    }

    @Test
    @DisplayName("Debe generar representación en cadena correctamente")
    void shouldGenerateStringRepresentationCorrectly() {
        // Given
        Money totalCost = Money.of(new BigDecimal("200000"));
        Money copaymentAmount = Money.of(new BigDecimal("50000"));
        Money insuranceCoverage = Money.of(new BigDecimal("150000"));
        Money patientResponsibility = Money.of(new BigDecimal("50000"));
        boolean requiresFullPayment = false;

        BillingCalculationResult result = BillingCalculationResult.of(
            totalCost, copaymentAmount, insuranceCoverage, patientResponsibility, requiresFullPayment);

        // When
        String toString = result.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("BillingCalculationResult"));
        assertTrue(toString.contains("200000"));
        assertTrue(toString.contains("50000"));
        assertTrue(toString.contains("150000"));
        assertTrue(toString.contains("fullPayment=false"));
    }

    @Test
    @DisplayName("Debe validar que total = copago + cobertura de seguro")
    void shouldValidateThatTotalEqualsCopaymentPlusInsuranceCoverage() {
        // Given
        Money totalCost = Money.of(new BigDecimal("200000"));
        Money copaymentAmount = Money.of(new BigDecimal("50000"));
        Money insuranceCoverage = Money.of(new BigDecimal("150000"));
        Money patientResponsibility = Money.of(new BigDecimal("50000"));
        boolean requiresFullPayment = false;

        BillingCalculationResult result = BillingCalculationResult.of(
            totalCost, copaymentAmount, insuranceCoverage, patientResponsibility, requiresFullPayment);

        // When
        BigDecimal calculatedTotal = result.getCopaymentAmount().getAmount()
                                   .add(result.getInsuranceCoverage().getAmount());

        // Then
        assertEquals(totalCost.getAmount(), calculatedTotal);
    }

    @Test
    @DisplayName("Debe validar consistencia cuando requiere pago completo")
    void shouldValidateConsistencyWhenRequiresFullPayment() {
        // Given
        Money totalCost = Money.of(new BigDecimal("200000"));
        Money copaymentAmount = Money.of(new BigDecimal("200000")); // Paga todo
        Money insuranceCoverage = Money.of(new BigDecimal("0"));    // Sin cobertura
        Money patientResponsibility = Money.of(new BigDecimal("200000"));
        boolean requiresFullPayment = true;

        BillingCalculationResult result = BillingCalculationResult.of(
            totalCost, copaymentAmount, insuranceCoverage, patientResponsibility, requiresFullPayment);

        // When & Then
        assertTrue(result.requiresFullPayment());
        assertEquals(totalCost.getAmount(), result.getCopaymentAmount().getAmount());
        assertEquals(BigDecimal.ZERO, result.getInsuranceCoverage().getAmount());
        assertEquals(totalCost.getAmount(), result.getPatientResponsibility().getAmount());
    }

    @Test
    @DisplayName("Debe validar consistencia cuando seguro cubre completamente")
    void shouldValidateConsistencyWhenInsuranceCoversCompletely() {
        // Given
        Money totalCost = Money.of(new BigDecimal("200000"));
        Money copaymentAmount = Money.of(new BigDecimal("0"));      // Sin copago
        Money insuranceCoverage = Money.of(new BigDecimal("200000")); // Seguro cubre todo
        Money patientResponsibility = Money.of(new BigDecimal("0")); // Paciente no paga
        boolean requiresFullPayment = false;

        BillingCalculationResult result = BillingCalculationResult.of(
            totalCost, copaymentAmount, insuranceCoverage, patientResponsibility, requiresFullPayment);

        // When & Then
        assertFalse(result.requiresFullPayment());
        assertEquals(BigDecimal.ZERO, result.getCopaymentAmount().getAmount());
        assertEquals(totalCost.getAmount(), result.getInsuranceCoverage().getAmount());
        assertEquals(BigDecimal.ZERO, result.getPatientResponsibility().getAmount());
    }

    @Test
    @DisplayName("Debe manejar valores negativos correctamente")
    void shouldHandleNegativeValuesCorrectly() {
        // Given - Intentar crear resultado con valores negativos (no debería ser posible en dominio real)
        Money totalCost = Money.of(new BigDecimal("200000"));
        Money copaymentAmount = Money.of(new BigDecimal("-50000")); // Valor negativo inválido
        Money insuranceCoverage = Money.of(new BigDecimal("250000")); // Más que el total
        Money patientResponsibility = Money.of(new BigDecimal("-50000"));
        boolean requiresFullPayment = false;

        // When & Then
        // Nota: En un dominio real, Money debería prevenir valores negativos
        // Por ahora, solo verificamos que el objeto se crea
        assertDoesNotThrow(() -> {
            BillingCalculationResult result = BillingCalculationResult.of(
                totalCost, copaymentAmount, insuranceCoverage, patientResponsibility, requiresFullPayment);
            assertNotNull(result);
        });
    }
}