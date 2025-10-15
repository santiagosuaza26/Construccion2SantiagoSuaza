package app.clinic.domain.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for domain exceptions to ensure proper error handling.
 */
class DomainExceptionsTest {

    @Test
    @DisplayName("Domain exceptions should be RuntimeExceptions")
    void domainExceptionsShouldBeRuntimeExceptions() {
        // Given
        String message = "Test domain exception";

        // When
        PatientAlreadyExistsException exception = new PatientAlreadyExistsException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Domain exceptions should maintain exception chain")
    void domainExceptionsShouldMaintainExceptionChain() {
        // Given
        String message = "Test domain exception";
        Throwable cause = new RuntimeException("Root cause");

        // When
        PatientAlreadyExistsException exception = new PatientAlreadyExistsException(message, cause);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("PatientAlreadyExistsException should create exception with message")
    void patientAlreadyExistsExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Patient already exists";

        // When
        PatientAlreadyExistsException exception = new PatientAlreadyExistsException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("UserAlreadyExistsException should create exception with message")
    void userAlreadyExistsExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "User already exists";

        // When
        UserAlreadyExistsException exception = new UserAlreadyExistsException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("InvalidPatientDataException should create exception with message")
    void invalidPatientDataExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Invalid patient data";

        // When
        InvalidPatientDataException exception = new InvalidPatientDataException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("InvalidUserDataException should create exception with message")
    void invalidUserDataExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Invalid user data";

        // When
        InvalidUserDataException exception = new InvalidUserDataException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("InvalidAppointmentDataException should create exception with message")
    void invalidAppointmentDataExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Invalid appointment data";

        // When
        InvalidAppointmentDataException exception = new InvalidAppointmentDataException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("DoctorNotAvailableException should create exception with message")
    void doctorNotAvailableExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Doctor not available";

        // When
        DoctorNotAvailableException exception = new DoctorNotAvailableException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("InvalidAppointmentTimeException should create exception with message")
    void invalidAppointmentTimeExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Invalid appointment time";

        // When
        InvalidAppointmentTimeException exception = new InvalidAppointmentTimeException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("OrderValidationException should create exception with message")
    void orderValidationExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Order validation failed";

        // When
        OrderValidationException exception = new OrderValidationException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("InsurancePolicyExpiredException should create exception with message")
    void insurancePolicyExpiredExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Insurance policy expired";

        // When
        InsurancePolicyExpiredException exception = new InsurancePolicyExpiredException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("CopaymentLimitExceededException should create exception with message")
    void copaymentLimitExceededExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Copayment limit exceeded";

        // When
        CopaymentLimitExceededException exception = new CopaymentLimitExceededException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("InvalidVitalSignsException should create exception with message")
    void invalidVitalSignsExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Invalid vital signs";

        // When
        InvalidVitalSignsException exception = new InvalidVitalSignsException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("MedicalRecordAlreadyExistsException should create exception with message")
    void medicalRecordAlreadyExistsExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Medical record already exists";

        // When
        MedicalRecordAlreadyExistsException exception = new MedicalRecordAlreadyExistsException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("InvalidMedicalRecordException should create exception with message")
    void invalidMedicalRecordExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Invalid medical record";

        // When
        InvalidMedicalRecordException exception = new InvalidMedicalRecordException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("InvalidBillingCalculationException should create exception with message")
    void invalidBillingCalculationExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Invalid billing calculation";

        // When
        InvalidBillingCalculationException exception = new InvalidBillingCalculationException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("InvalidEmergencyContactException should create exception with message")
    void invalidEmergencyContactExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Invalid emergency contact";

        // When
        InvalidEmergencyContactException exception = new InvalidEmergencyContactException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("InvalidInsurancePolicyException should create exception with message")
    void invalidInsurancePolicyExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Invalid insurance policy";

        // When
        InvalidInsurancePolicyException exception = new InvalidInsurancePolicyException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("InvalidPatientVisitDataException should create exception with message")
    void invalidPatientVisitDataExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Invalid patient visit data";

        // When
        InvalidPatientVisitDataException exception = new InvalidPatientVisitDataException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("InvalidInventoryItemException should create exception with message")
    void invalidInventoryItemExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Invalid inventory item";

        // When
        InvalidInventoryItemException exception = new InvalidInventoryItemException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("InvalidValueObjectException should create exception with message")
    void invalidValueObjectExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Invalid value object";

        // When
        InvalidValueObjectException exception = new InvalidValueObjectException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("EntityNotFoundException should create exception with message")
    void entityNotFoundExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Entity not found";

        // When
        EntityNotFoundException exception = new EntityNotFoundException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("BusinessRuleViolationException should create exception with message")
    void businessRuleViolationExceptionShouldCreateExceptionWithMessage() {
        // Given
        String message = "Business rule violation";

        // When
        BusinessRuleViolationException exception = new BusinessRuleViolationException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof DomainException);
    }

    @Test
    @DisplayName("All domain exceptions should be RuntimeExceptions")
    void allDomainExceptionsShouldBeRuntimeExceptions() {
        // This test ensures that all domain exceptions are unchecked exceptions

        // Given
        PatientAlreadyExistsException patientException = new PatientAlreadyExistsException("test");
        UserAlreadyExistsException userException = new UserAlreadyExistsException("test");
        InvalidPatientDataException invalidDataException = new InvalidPatientDataException("test");

        // Then
        assertTrue(patientException instanceof RuntimeException);
        assertTrue(userException instanceof RuntimeException);
        assertTrue(invalidDataException instanceof RuntimeException);
    }

}