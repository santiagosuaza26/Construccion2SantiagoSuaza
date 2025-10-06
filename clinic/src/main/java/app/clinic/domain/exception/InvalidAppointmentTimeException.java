package app.clinic.domain.exception;

/**
 * Exception thrown when attempting to schedule an appointment at an invalid time.
 */
public class InvalidAppointmentTimeException extends DomainException {

    public InvalidAppointmentTimeException(String message) {
        super(message);
    }

    public InvalidAppointmentTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}