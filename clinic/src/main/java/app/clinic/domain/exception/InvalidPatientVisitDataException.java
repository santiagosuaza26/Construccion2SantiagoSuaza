package app.clinic.domain.exception;

/**
 * Exception thrown when patient visit data is invalid.
 */
public class InvalidPatientVisitDataException extends DomainException {

    public InvalidPatientVisitDataException(String message) {
        super(message);
    }

    public InvalidPatientVisitDataException(String message, Throwable cause) {
        super(message, cause);
    }
}