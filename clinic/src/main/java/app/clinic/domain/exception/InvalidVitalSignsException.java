
package app.clinic.domain.exception;

/**
 * Exception thrown when vital signs values are outside valid ranges.
 */
public class InvalidVitalSignsException extends DomainException {

    public InvalidVitalSignsException(String message) {
        super(message);
    }

    public InvalidVitalSignsException(String message, Throwable cause) {
        super(message, cause);
    }
}