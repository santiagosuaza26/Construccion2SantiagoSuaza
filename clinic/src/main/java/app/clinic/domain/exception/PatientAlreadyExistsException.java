package app.clinic.domain.exception;

/**
 * Exception thrown when attempting to register a patient that already exists.
 */
public class PatientAlreadyExistsException extends DomainException {

    public PatientAlreadyExistsException(String message) {
        super(message);
    }

    public PatientAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}