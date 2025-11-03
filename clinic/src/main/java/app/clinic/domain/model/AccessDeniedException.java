package app.clinic.domain.model;

public class AccessDeniedException extends DomainException {
    public AccessDeniedException(String message) {
        super("Access denied: " + message);
    }
}