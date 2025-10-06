package app.clinic.domain.exception;

/**
 * Base exception for all domain-specific exceptions.
 * Represents business rule violations and domain constraints.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}