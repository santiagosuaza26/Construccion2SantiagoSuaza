package app.clinic.domain.service;

import app.clinic.domain.model.valueobject.Credentials;

public interface UserValidationService {
    void validateCredentialsUniqueness(Credentials credentials);
    void validateUserData(String fullName, String identificationNumber, String email, String phone,
                         String dateOfBirth, String address, String role, String username, String password);
}