package app.clinic.domain.exception;

/**
 * Exception thrown when an inventory item has invalid data.
 */
public class InvalidInventoryItemException extends DomainException {

    public InvalidInventoryItemException(String message) {
        super(message);
    }

    public InvalidInventoryItemException(String message, Throwable cause) {
        super(message, cause);
    }
}