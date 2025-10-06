package app.clinic.domain.exception;

/**
 * Exception thrown when attempting to use an expired insurance policy.
 */
public class InsurancePolicyExpiredException extends DomainException {

    public InsurancePolicyExpiredException(String message) {
        super(message);
    }

    public InsurancePolicyExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}