package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.application.usecase.AuthenticateUserUseCase;
import app.clinic.domain.service.UserService;

@Service
public class AuthenticateUserUseCaseService extends AuthenticateUserUseCase {

    public AuthenticateUserUseCaseService(UserService userService) {
        super(userService);
    }

    // Infrastructure layer service that extends the domain use case
    // Can add infrastructure-specific concerns like logging, caching, etc.
}