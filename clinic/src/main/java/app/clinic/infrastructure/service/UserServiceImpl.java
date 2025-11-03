package app.clinic.infrastructure.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.User;
import app.clinic.domain.repository.UserRepository;
import app.clinic.domain.service.RoleBasedAccessService;
import app.clinic.domain.service.UserService;
import app.clinic.domain.service.UserValidationService;

@Service
public class UserServiceImpl extends UserService {

    public UserServiceImpl(UserRepository userRepository, UserValidationService validationService, RoleBasedAccessService roleBasedAccessService) {
        super(userRepository, validationService, roleBasedAccessService);
    }

    // Infrastructure layer service that extends the domain service
    // Can add infrastructure-specific concerns like logging, caching, etc.

    @Override
    @Cacheable(value = "users", key = "#username")
    public User findUserByUsername(String username) {
        return super.findUserByUsername(username);
    }
}