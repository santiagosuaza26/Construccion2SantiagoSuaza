package app.clinic.domain.exception;

/**
 * Exception thrown when an order violates business rules
 * (e.g., mixing diagnostic aids with medications or procedures).
 */
public class InvalidOrderRuleException extends DomainException {

    public InvalidOrderRuleException(String message) {
        super(message);
    }

    public InvalidOrderRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}