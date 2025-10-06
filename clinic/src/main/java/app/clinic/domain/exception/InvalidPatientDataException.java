package app.clinic.domain.exception;

/**
 * Exception thrown when patient data is invalid.
 */
public class InvalidPatientDataException extends DomainException {

    public InvalidPatientDataException(String message) {
        super(message);
    }

    public InvalidPatientDataException(String message, Throwable cause) {
        super(message, cause);
    }
}