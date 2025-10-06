package app.clinic.domain.exception;

/**
 * Exception thrown when insurance policy data is invalid.
 */
public class InvalidInsurancePolicyException extends DomainException {

    public InvalidInsurancePolicyException(String message) {
        super(message);
    }

    public InvalidInsurancePolicyException(String message, Throwable cause) {
        super(message, cause);
    }
}