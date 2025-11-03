package app.clinic.domain.model;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(String userId) {
        super("User not found with ID: " + userId);
    }
}