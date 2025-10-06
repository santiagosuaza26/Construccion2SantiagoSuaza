package app.clinic.domain.exception;

/**
 * Exception thrown when medical record data is invalid.
 */
public class InvalidMedicalRecordException extends DomainException {

    public InvalidMedicalRecordException(String message) {
        super(message);
    }

    public InvalidMedicalRecordException(String message, Throwable cause) {
        super(message, cause);
    }
}