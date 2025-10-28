package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.repository.UserRepository;
import app.clinic.domain.service.UserService;
import app.clinic.domain.service.UserValidationService;

@Service
public class UserServiceImpl extends UserService {

    public UserServiceImpl(UserRepository userRepository, UserValidationService validationService) {
        super(userRepository, validationService);
    }

    // Infrastructure layer service that extends the domain service
    // Can add infrastructure-specific concerns like logging, caching, etc.
}