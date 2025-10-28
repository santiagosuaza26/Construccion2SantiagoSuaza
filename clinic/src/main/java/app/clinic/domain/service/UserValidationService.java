package app.clinic.domain.service;

import app.clinic.domain.model.valueobject.Credentials;

public interface UserValidationService {
    void validateCredentialsUniqueness(Credentials credentials);
}