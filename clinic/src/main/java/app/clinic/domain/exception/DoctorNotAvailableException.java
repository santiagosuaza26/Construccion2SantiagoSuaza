package app.clinic.domain.exception;

/**
 * Exception thrown when attempting to schedule an appointment with a doctor who is not available.
 */
public class DoctorNotAvailableException extends DomainException {

    public DoctorNotAvailableException(String message) {
        super(message);
    }

    public DoctorNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}