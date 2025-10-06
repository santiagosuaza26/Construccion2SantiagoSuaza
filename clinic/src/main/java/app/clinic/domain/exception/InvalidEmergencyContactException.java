package app.clinic.domain.exception;

/**
 * Exception thrown when emergency contact data is invalid.
 */
public class InvalidEmergencyContactException extends DomainException {

    public InvalidEmergencyContactException(String message) {
        super(message);
    }

    public InvalidEmergencyContactException(String message, Throwable cause) {
        super(message, cause);
    }
}