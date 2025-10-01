package app.domain.services;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import app.domain.exception.DomainValidationException;
import app.domain.model.InsurancePolicy;

/**
 * Pruebas unitarias para InsurancePolicyValidationService
 * Demuestra el cumplimiento de principios SOLID:
 * - SRP: Una sola responsabilidad (validar pólizas de seguro)
 */
class InsurancePolicyValidationServiceTest {

    private InsurancePolicyValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new InsurancePolicyValidationService();
    }

    @Nested
    @DisplayName("Validaciones básicas de póliza")
    class BasicPolicyValidationTests {

        @Test
        @DisplayName("Debe lanzar excepción cuando la póliza es null")
        void shouldThrowExceptionWhenPolicyIsNull() {
            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(null)
            );

            assertEquals("Insurance policy cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar compañía requerida")
        void shouldValidateCompanyRequired() {
            // Given
            InsurancePolicy policyWithoutCompany = new InsurancePolicy(null, "POL123456", true, LocalDate.now().plusMonths(6));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(policyWithoutCompany)
            );

            assertEquals("Insurance company is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar número de póliza requerido")
        void shouldValidatePolicyNumberRequired() {
            // Given
            InsurancePolicy policyWithoutNumber = new InsurancePolicy("Seguros ABC", null, true, LocalDate.now().plusMonths(6));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(policyWithoutNumber)
            );

            assertEquals("Policy number is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe validar fecha de vencimiento requerida")
        void shouldValidateEndDateRequired() {
            // Given
            InsurancePolicy policyWithoutEndDate = new InsurancePolicy("Seguros ABC", "POL123456", true, null);

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(policyWithoutEndDate)
            );

            assertEquals("Policy end date is required", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Validaciones de compañía")
    class CompanyValidationTests {

        @Test
        @DisplayName("Debe rechazar compañía vacía")
        void shouldRejectEmptyCompany() {
            // Given
            InsurancePolicy policyWithEmptyCompany = new InsurancePolicy("", "POL123456", true, LocalDate.now().plusMonths(6));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(policyWithEmptyCompany)
            );

            assertEquals("Insurance company is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar compañía muy larga")
        void shouldRejectCompanyTooLong() {
            // Given
            String longCompany = "A".repeat(101); // Más de 100 caracteres
            InsurancePolicy policyWithLongCompany = new InsurancePolicy(longCompany, "POL123456", true, LocalDate.now().plusMonths(6));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(policyWithLongCompany)
            );

            assertEquals("Insurance company name cannot exceed 100 characters", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar compañía con caracteres inválidos")
        void shouldRejectCompanyWithInvalidCharacters() {
            // Given
            InsurancePolicy policyWithInvalidChars = new InsurancePolicy("Seguros@ABC#123", "POL123456", true, LocalDate.now().plusMonths(6));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(policyWithInvalidChars)
            );

            assertEquals("Insurance company name can only contain letters, numbers and spaces", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar compañía válida")
        void shouldAcceptValidCompany() {
            // Given
            InsurancePolicy policyWithValidCompany = new InsurancePolicy("Seguros ABC 123", "POL123456", true, LocalDate.now().plusMonths(6));

            // When & Then
            assertDoesNotThrow(() -> validationService.validateInsurancePolicy(policyWithValidCompany));
        }

        @Test
        @DisplayName("Debe aceptar compañía en límite de longitud")
        void shouldAcceptCompanyAtLengthLimit() {
            // Given
            String companyAtLimit = "A".repeat(100);
            InsurancePolicy policyAtLimit = new InsurancePolicy(companyAtLimit, "POL123456", true, LocalDate.now().plusMonths(6));

            // When & Then
            assertDoesNotThrow(() -> validationService.validateInsurancePolicy(policyAtLimit));
        }
    }

    @Nested
    @DisplayName("Validaciones de número de póliza")
    class PolicyNumberValidationTests {

        @Test
        @DisplayName("Debe rechazar número de póliza vacío")
        void shouldRejectEmptyPolicyNumber() {
            // Given
            InsurancePolicy policyWithEmptyNumber = new InsurancePolicy("Seguros ABC", "", true, LocalDate.now().plusMonths(6));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(policyWithEmptyNumber)
            );

            assertEquals("Policy number is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar número de póliza muy largo")
        void shouldRejectPolicyNumberTooLong() {
            // Given
            String longPolicyNumber = "A".repeat(51); // Más de 50 caracteres
            InsurancePolicy policyWithLongNumber = new InsurancePolicy("Seguros ABC", longPolicyNumber, true, LocalDate.now().plusMonths(6));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(policyWithLongNumber)
            );

            assertEquals("Policy number cannot exceed 50 characters", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar número de póliza con caracteres inválidos")
        void shouldRejectPolicyNumberWithInvalidCharacters() {
            // Given
            InsurancePolicy policyWithInvalidNumber = new InsurancePolicy("Seguros ABC", "POL@123#456", true, LocalDate.now().plusMonths(6));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(policyWithInvalidNumber)
            );

            assertEquals("Policy number can only contain letters, numbers, hyphens and forward slashes", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar número de póliza válido")
        void shouldAcceptValidPolicyNumber() {
            // Given
            InsurancePolicy policyWithValidNumber = new InsurancePolicy("Seguros ABC", "POL-123/456", true, LocalDate.now().plusMonths(6));

            // When & Then
            assertDoesNotThrow(() -> validationService.validateInsurancePolicy(policyWithValidNumber));
        }

        @Test
        @DisplayName("Debe aceptar número de póliza en límite de longitud")
        void shouldAcceptPolicyNumberAtLengthLimit() {
            // Given
            String numberAtLimit = "A".repeat(50);
            InsurancePolicy policyAtLimit = new InsurancePolicy("Seguros ABC", numberAtLimit, true, LocalDate.now().plusMonths(6));

            // When & Then
            assertDoesNotThrow(() -> validationService.validateInsurancePolicy(policyAtLimit));
        }
    }

    @Nested
    @DisplayName("Validaciones de fecha de vencimiento")
    class EndDateValidationTests {

        @Test
        @DisplayName("Debe rechazar fecha en el pasado")
        void shouldRejectPastEndDate() {
            // Given
            InsurancePolicy policyWithPastDate = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now().minusDays(1));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(policyWithPastDate)
            );

            assertEquals("Active policy cannot have end date in the past", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar fecha muy lejana en el futuro")
        void shouldRejectTooFutureEndDate() {
            // Given
            InsurancePolicy policyWithFarFutureDate = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now().plusYears(11));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(policyWithFarFutureDate)
            );

            assertEquals("Policy end date cannot be more than 10 years in the future", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar fecha válida en el futuro")
        void shouldAcceptValidFutureEndDate() {
            // Given
            InsurancePolicy policyWithValidDate = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now().plusMonths(6));

            // When & Then
            assertDoesNotThrow(() -> validationService.validateInsurancePolicy(policyWithValidDate));
        }

        @Test
        @DisplayName("Debe aceptar fecha en límite de 10 años")
        void shouldAcceptEndDateAt10YearLimit() {
            // Given
            InsurancePolicy policyAt10YearLimit = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now().plusYears(10));

            // When & Then
            assertDoesNotThrow(() -> validationService.validateInsurancePolicy(policyAt10YearLimit));
        }
    }

    @Nested
    @DisplayName("Validaciones de estado de póliza")
    class PolicyStatusValidationTests {

        @Test
        @DisplayName("Debe rechazar póliza activa con fecha pasada")
        void shouldRejectActivePolicyWithPastDate() {
            // Given
            InsurancePolicy activePolicyWithPastDate = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now().minusDays(1));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(activePolicyWithPastDate)
            );

            assertEquals("Active policy cannot have end date in the past", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar póliza inactiva con fecha futura")
        void shouldRejectInactivePolicyWithFutureDate() {
            // Given
            InsurancePolicy inactivePolicyWithFutureDate = new InsurancePolicy("Seguros ABC", "POL123456", false, LocalDate.now().plusMonths(6));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(inactivePolicyWithFutureDate)
            );

            assertEquals("Inactive policy cannot have end date in the future", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar póliza activa con fecha futura")
        void shouldAcceptActivePolicyWithFutureDate() {
            // Given
            InsurancePolicy activePolicyWithFutureDate = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now().plusMonths(6));

            // When & Then
            assertDoesNotThrow(() -> validationService.validateInsurancePolicy(activePolicyWithFutureDate));
        }

        @Test
        @DisplayName("Debe aceptar póliza inactiva con fecha pasada")
        void shouldAcceptInactivePolicyWithPastDate() {
            // Given
            InsurancePolicy inactivePolicyWithPastDate = new InsurancePolicy("Seguros ABC 123", "POL123456", false, LocalDate.now().minusDays(1));

            // When & Then
            assertDoesNotThrow(() -> validationService.validateInsurancePolicy(inactivePolicyWithPastDate));
        }
    }

    @Nested
    @DisplayName("Validaciones de póliza activa")
    class ActivePolicyValidationTests {

        @Test
        @DisplayName("Debe rechazar póliza nula")
        void shouldRejectNullPolicy() {
            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validatePolicyIsActive(null)
            );

            assertEquals("Insurance policy is required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar póliza inactiva")
        void shouldRejectInactivePolicy() {
            // Given
            InsurancePolicy inactivePolicy = new InsurancePolicy("Seguros ABC", "POL123456", false, LocalDate.now().plusMonths(6));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validatePolicyIsActive(inactivePolicy)
            );

            assertEquals("Insurance policy must be active", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar póliza activa expirada")
        void shouldRejectActiveExpiredPolicy() {
            // Given
            InsurancePolicy activeExpiredPolicy = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now().minusDays(1));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validatePolicyIsActive(activeExpiredPolicy)
            );

            assertEquals("Insurance policy has expired", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar póliza activa válida")
        void shouldAcceptActiveValidPolicy() {
            // Given
            InsurancePolicy activeValidPolicy = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now().plusMonths(6));

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePolicyIsActive(activeValidPolicy));
        }
    }

    @Nested
    @DisplayName("Validaciones de cobertura mínima")
    class MinimumCoverageValidationTests {

        @Test
        @DisplayName("Debe rechazar cobertura básica")
        void shouldRejectBasicCoverage() {
            // Given
            InsurancePolicy basicCoveragePolicy = new InsurancePolicy("Seguros Cobertura Basica", "POL123456", true, LocalDate.now().plusMonths(6));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateMinimumCoverage(basicCoveragePolicy)
            );

            assertEquals("Insurance policy must provide comprehensive coverage", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar cobertura mínima")
        void shouldRejectMinimumCoverage() {
            // Given
            InsurancePolicy minimumCoveragePolicy = new InsurancePolicy("Seguros Cobertura Minima", "POL123456", true, LocalDate.now().plusMonths(6));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateMinimumCoverage(minimumCoveragePolicy)
            );

            assertEquals("Insurance policy must provide comprehensive coverage", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar cobertura comprehensiva")
        void shouldAcceptComprehensiveCoverage() {
            // Given
            InsurancePolicy comprehensivePolicy = new InsurancePolicy("Seguros Premium Plus", "POL123456", true, LocalDate.now().plusMonths(6));

            // When & Then
            assertDoesNotThrow(() -> validationService.validateMinimumCoverage(comprehensivePolicy));
        }
    }

    @Nested
    @DisplayName("Validaciones de renovación")
    class RenewalValidationTests {

        @Test
        @DisplayName("Debe rechazar cuando alguna póliza es null")
        void shouldRejectWhenAnyPolicyIsNull() {
            // Given
            InsurancePolicy currentPolicy = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now().plusMonths(6));
            InsurancePolicy renewedPolicy = null;

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validatePolicyRenewal(currentPolicy, renewedPolicy)
            );

            assertEquals("Both current and renewed policies are required", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar cambio de compañía durante renovación")
        void shouldRejectCompanyChangeDuringRenewal() {
            // Given
            InsurancePolicy currentPolicy = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now().plusMonths(6));
            InsurancePolicy renewedPolicy = new InsurancePolicy("Seguros XYZ", "POL123456", true, LocalDate.now().plusMonths(12));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validatePolicyRenewal(currentPolicy, renewedPolicy)
            );

            assertEquals("Cannot change insurance company during renewal", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar fecha de renovación anterior")
        void shouldRejectEarlierRenewalDate() {
            // Given
            InsurancePolicy currentPolicy = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now().plusYears(2));
            InsurancePolicy renewedPolicy = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now().plusYears(1));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validatePolicyRenewal(currentPolicy, renewedPolicy)
            );

            assertEquals("Renewed policy must have end date after current policy", exception.getMessage());
        }

        @Test
        @DisplayName("Debe aceptar renovación válida")
        void shouldAcceptValidRenewal() {
            // Given
            InsurancePolicy currentPolicy = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now().plusMonths(6));
            InsurancePolicy renewedPolicy = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now().plusMonths(12));

            // When & Then
            assertDoesNotThrow(() -> validationService.validatePolicyRenewal(currentPolicy, renewedPolicy));
        }
    }

    @Nested
    @DisplayName("Casos extremos y edge cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("Debe manejar compañía con múltiples espacios")
        void shouldHandleCompanyWithMultipleSpaces() {
            // Given
            InsurancePolicy policyWithMultipleSpaces = new InsurancePolicy("Seguros  ABC  123", "POL123456", true, LocalDate.now().plusMonths(6));

            // When & Then
            assertDoesNotThrow(() -> validationService.validateInsurancePolicy(policyWithMultipleSpaces));
        }

        @Test
        @DisplayName("Debe manejar número de póliza con múltiples caracteres especiales")
        void shouldHandlePolicyNumberWithMultipleSpecialChars() {
            // Given
            InsurancePolicy policyWithMultipleSpecials = new InsurancePolicy("Seguros ABC", "POL-123/456-789", true, LocalDate.now().plusMonths(6));

            // When & Then
            assertDoesNotThrow(() -> validationService.validateInsurancePolicy(policyWithMultipleSpecials));
        }

        @Test
        @DisplayName("Debe manejar fecha exactamente hoy")
        void shouldHandleTodayDate() {
            // Given
            InsurancePolicy policyWithTodayDate = new InsurancePolicy("Seguros ABC", "POL123456", true, LocalDate.now());

            // When & Then
            assertDoesNotThrow(() -> validationService.validateInsurancePolicy(policyWithTodayDate));
        }

        @Test
        @DisplayName("Debe rechazar compañía con caracteres especiales")
        void shouldRejectCompanyWithSpecialCharacters() {
            // Given
            InsurancePolicy policyWithSpecialCompany = new InsurancePolicy("Seguros@ABC#123", "POL123456", true, LocalDate.now().plusMonths(6));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(policyWithSpecialCompany)
            );

            assertEquals("Insurance company name can only contain letters, numbers and spaces", exception.getMessage());
        }

        @Test
        @DisplayName("Debe rechazar número de póliza con caracteres no permitidos")
        void shouldRejectPolicyNumberWithDisallowedCharacters() {
            // Given
            InsurancePolicy policyWithDisallowedChars = new InsurancePolicy("Seguros ABC", "POL@123#456", true, LocalDate.now().plusMonths(6));

            // When & Then
            DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> validationService.validateInsurancePolicy(policyWithDisallowedChars)
            );

            assertEquals("Policy number can only contain letters, numbers, hyphens and forward slashes", exception.getMessage());
        }
    }
}