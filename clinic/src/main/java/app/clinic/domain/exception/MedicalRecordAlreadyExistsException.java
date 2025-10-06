package app.clinic.domain.exception;

/**
 * Exception thrown when attempting to create a medical record that already exists.
 */
public class MedicalRecordAlreadyExistsException extends DomainException {

    public MedicalRecordAlreadyExistsException(String message) {
        super(message);
    }

    public MedicalRecordAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}