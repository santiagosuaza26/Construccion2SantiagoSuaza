package app.clinic.domain.exception;

/**
 * Exception thrown when a patient has exceeded the maximum copayment limit for the year.
 */
public class CopaymentLimitExceededException extends DomainException {

    public CopaymentLimitExceededException(String message) {
        super(message);
    }

    public CopaymentLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}