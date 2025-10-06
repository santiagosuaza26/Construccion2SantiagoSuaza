package app.clinic.domain.exception;

/**
 * Exception thrown when appointment data is invalid.
 */
public class InvalidAppointmentDataException extends DomainException {

    public InvalidAppointmentDataException(String message) {
        super(message);
    }

    public InvalidAppointmentDataException(String message, Throwable cause) {
        super(message, cause);
    }
}