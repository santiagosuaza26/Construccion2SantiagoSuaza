package app.clinic.domain.exception;

/**
 * Exception thrown when billing calculation fails due to invalid data.
 */
public class InvalidBillingCalculationException extends DomainException {

    public InvalidBillingCalculationException(String message) {
        super(message);
    }

    public InvalidBillingCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}