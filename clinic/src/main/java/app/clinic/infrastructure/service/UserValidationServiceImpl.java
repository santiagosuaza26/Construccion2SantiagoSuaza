package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.valueobject.Credentials;
import app.clinic.domain.repository.UserRepository;
import app.clinic.domain.service.UserValidationService;

@Service
public class UserValidationServiceImpl implements UserValidationService {

    private final UserRepository userRepository;

    public UserValidationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validateCredentialsUniqueness(Credentials credentials) {
        // Validar que el username sea único
        if (userRepository.findByUsername(credentials.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + credentials.getUsername().getValue());
        }
    }
}