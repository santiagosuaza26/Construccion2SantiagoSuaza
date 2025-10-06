package app.clinic.domain.exception;

/**
 * Exception thrown when user data is invalid.
 */
public class InvalidUserDataException extends DomainException {

    public InvalidUserDataException(String message) {
        super(message);
    }

    public InvalidUserDataException(String message, Throwable cause) {
        super(message, cause);
    }
}