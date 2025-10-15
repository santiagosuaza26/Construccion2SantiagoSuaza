package app.clinic.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * Pruebas para excepciones personalizadas del dominio médico.
 * Valida que las excepciones se creen correctamente con mensajes apropiados.
 */
@DisplayName("Custom Domain Exceptions Tests")
class CustomExceptionsTest {

    @Nested
    @DisplayName("Excepciones de Validación de Datos")
    class DataValidationExceptionsTests {

        @Test
        @DisplayName("Debe crear excepción de paciente inválido correctamente")
        void shouldCreateInvalidPatientDataExceptionCorrectly() {
            // Given
            String errorMessage = "Patient data is invalid";

            // When
            InvalidPatientDataException exception = new InvalidPatientDataException(errorMessage);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(errorMessage);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de usuario inválido correctamente")
        void shouldCreateInvalidUserDataExceptionCorrectly() {
            // Given
            String errorMessage = "User data is invalid";

            // When
            InvalidUserDataException exception = new InvalidUserDataException(errorMessage);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(errorMessage);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de cita inválida correctamente")
        void shouldCreateInvalidAppointmentDataExceptionCorrectly() {
            // Given
            String errorMessage = "Appointment data is invalid";

            // When
            InvalidAppointmentDataException exception = new InvalidAppointmentDataException(errorMessage);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(errorMessage);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de póliza de seguro inválida correctamente")
        void shouldCreateInvalidInsurancePolicyExceptionCorrectly() {
            // Given
            String errorMessage = "Insurance policy data is invalid";

            // When
            InvalidInsurancePolicyException exception = new InvalidInsurancePolicyException(errorMessage);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(errorMessage);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de contacto de emergencia inválido correctamente")
        void shouldCreateInvalidEmergencyContactExceptionCorrectly() {
            // Given
            String errorMessage = "Emergency contact data is invalid";

            // When
            InvalidEmergencyContactException exception = new InvalidEmergencyContactException(errorMessage);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(errorMessage);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de signos vitales inválidos correctamente")
        void shouldCreateInvalidVitalSignsExceptionCorrectly() {
            // Given
            String errorMessage = "Vital signs data is invalid";

            // When
            InvalidVitalSignsException exception = new InvalidVitalSignsException(errorMessage);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(errorMessage);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de Value Object inválido correctamente")
        void shouldCreateInvalidValueObjectExceptionCorrectly() {
            // Given
            String errorMessage = "Value object data is invalid";

            // When
            InvalidValueObjectException exception = new InvalidValueObjectException(errorMessage);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(errorMessage);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("Excepciones de Entidades Existentes")
    class ExistingEntityExceptionsTests {

        @Test
        @DisplayName("Debe crear excepción de paciente ya existente correctamente")
        void shouldCreatePatientAlreadyExistsExceptionCorrectly() {
            // Given
            String cedula = "12345678";

            // When
            PatientAlreadyExistsException exception = new PatientAlreadyExistsException(cedula);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).contains(cedula);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de usuario ya existente correctamente")
        void shouldCreateUserAlreadyExistsExceptionCorrectly() {
            // Given
            String username = "existinguser";

            // When
            UserAlreadyExistsException exception = new UserAlreadyExistsException(username);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).contains(username);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de registro médico ya existente correctamente")
        void shouldCreateMedicalRecordAlreadyExistsExceptionCorrectly() {
            // Given
            String errorMessage = "Medical record already exists for patient patient-123 with type diagnosis";

            // When
            MedicalRecordAlreadyExistsException exception = new MedicalRecordAlreadyExistsException(errorMessage);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(errorMessage);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("Excepciones de Negocio")
    class BusinessExceptionsTests {

        @Test
        @DisplayName("Debe crear excepción de límite de copago excedido correctamente")
        void shouldCreateCopaymentLimitExceededExceptionCorrectly() {
            // Given
            String errorMessage = "Patient has exceeded the maximum copayment limit of 1000000 with current amount of 1500000";

            // When
            CopaymentLimitExceededException exception = new CopaymentLimitExceededException(errorMessage);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(errorMessage);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de doctor no disponible correctamente")
        void shouldCreateDoctorNotAvailableExceptionCorrectly() {
            // Given
            String errorMessage = "Doctor with cedula 87654321 is not available at 2024-12-25T10:00:00";

            // When
            DoctorNotAvailableException exception = new DoctorNotAvailableException(errorMessage);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(errorMessage);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de póliza de seguro expirada correctamente")
        void shouldCreateInsurancePolicyExpiredExceptionCorrectly() {
            // Given
            String policyNumber = "POL-001";

            // When
            InsurancePolicyExpiredException exception = new InsurancePolicyExpiredException(policyNumber);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).contains(policyNumber);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de violación de regla de negocio correctamente")
        void shouldCreateBusinessRuleViolationExceptionCorrectly() {
            // Given
            String ruleDescription = "Maximum one emergency contact per patient";

            // When
            BusinessRuleViolationException exception = new BusinessRuleViolationException(ruleDescription);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(ruleDescription);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de entidad no encontrada correctamente")
        void shouldCreateEntityNotFoundExceptionCorrectly() {
            // Given
            String errorMessage = "Patient with identifier 12345678 not found";

            // When
            EntityNotFoundException exception = new EntityNotFoundException(errorMessage);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(errorMessage);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("Excepciones de Procesos Médicos")
    class MedicalProcessExceptionsTests {

        @Test
        @DisplayName("Debe crear excepción de cálculo de facturación inválido correctamente")
        void shouldCreateInvalidBillingCalculationExceptionCorrectly() {
            // Given
            String calculationError = "Billing calculation failed due to invalid data";

            // When
            InvalidBillingCalculationException exception = new InvalidBillingCalculationException(calculationError);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(calculationError);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de registro médico inválido correctamente")
        void shouldCreateInvalidMedicalRecordExceptionCorrectly() {
            // Given
            String recordError = "Medical record data is invalid";

            // When
            InvalidMedicalRecordException exception = new InvalidMedicalRecordException(recordError);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(recordError);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de visita de paciente inválida correctamente")
        void shouldCreateInvalidPatientVisitDataExceptionCorrectly() {
            // Given
            String visitError = "Patient visit data is invalid";

            // When
            InvalidPatientVisitDataException exception = new InvalidPatientVisitDataException(visitError);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(visitError);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de horario de cita inválido correctamente")
        void shouldCreateInvalidAppointmentTimeExceptionCorrectly() {
            // Given
            String timeError = "Appointment time is invalid";

            // When
            InvalidAppointmentTimeException exception = new InvalidAppointmentTimeException(timeError);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(timeError);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("Excepciones de Inventario y Órdenes")
    class InventoryAndOrderExceptionsTests {

        @Test
        @DisplayName("Debe crear excepción de item de inventario inválido correctamente")
        void shouldCreateInvalidInventoryItemExceptionCorrectly() {
            // Given
            String itemError = "Inventory item data is invalid";

            // When
            InvalidInventoryItemException exception = new InvalidInventoryItemException(itemError);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(itemError);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de regla de orden inválida correctamente")
        void shouldCreateInvalidOrderRuleExceptionCorrectly() {
            // Given
            String ruleError = "Order rule is invalid";

            // When
            InvalidOrderRuleException exception = new InvalidOrderRuleException(ruleError);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(ruleError);
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("Debe crear excepción de validación de orden correctamente")
        void shouldCreateOrderValidationExceptionCorrectly() {
            // Given
            String validationError = "Order validation failed";

            // When
            OrderValidationException exception = new OrderValidationException(validationError);

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(validationError);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("Jerarquía de Excepciones")
    class ExceptionHierarchyTests {

        @Test
        @DisplayName("Todas las excepciones personalizadas deben extender DomainException")
        void allCustomExceptionsShouldExtendDomainException() {
            // Given
            DomainException[] exceptions = {
                new InvalidPatientDataException("test"),
                new PatientAlreadyExistsException("test"),
                new CopaymentLimitExceededException("Copayment limit exceeded"),
                new DoctorNotAvailableException("Doctor not available"),
                new InsurancePolicyExpiredException("POL-001"),
                new BusinessRuleViolationException("test rule"),
                new EntityNotFoundException("Patient not found"),
                new InvalidBillingCalculationException("test"),
                new InvalidMedicalRecordException("test"),
                new InvalidPatientVisitDataException("test"),
                new InvalidAppointmentTimeException("test"),
                new InvalidInventoryItemException("test"),
                new InvalidOrderRuleException("test"),
                new OrderValidationException("test")
            };

            // Then
            for (DomainException exception : exceptions) {
                assertThat(exception).isInstanceOf(DomainException.class);
            }
        }

        @Test
        @DisplayName("Debe mantener la cadena de excepciones correctamente")
        void shouldMaintainExceptionChainCorrectly() {
            // Given
            String rootCause = "Root cause";
            RuntimeException cause = new RuntimeException(rootCause);

            // When
            DomainException exception = new InvalidPatientDataException("Wrapper message", cause);

            // Then
            assertThat(exception).isInstanceOf(DomainException.class);
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.getCause().getMessage()).isEqualTo(rootCause);
        }
    }
}