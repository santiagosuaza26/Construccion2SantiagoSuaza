package app.clinic.domain.exception;

/**
 * Exception thrown when a value object has invalid data.
 */
public class InvalidValueObjectException extends DomainException {

    public InvalidValueObjectException(String message) {
        super(message);
    }

    public InvalidValueObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}