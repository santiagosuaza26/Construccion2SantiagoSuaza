package app.clinic.domain.exception;

/**
 * Exception thrown when an order fails validation rules.
 */
public class OrderValidationException extends DomainException {

    public OrderValidationException(String message) {
        super(message);
    }

    public OrderValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}