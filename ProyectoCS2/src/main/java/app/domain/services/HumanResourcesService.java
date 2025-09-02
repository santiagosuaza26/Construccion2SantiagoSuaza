package app.domain.services;

import app.domain.model.Role;
import app.domain.model.User;
import app.domain.port.UserRepository;

public class HumanResourcesService {
    private final UserRepository userRepository;

    public HumanResourcesService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        ensureUserPermissions(user.getRole());
        if (userRepository.existsByIdCard(user.getIdCard())) {
            throw new IllegalArgumentException("User idCard already exists");
        }
        if (userRepository.existsByUsername(user.getCredentials().getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        return userRepository.save(user);
    }

    public void deleteUser(String idCard) {
        userRepository.deleteByIdCard(idCard);
    }

    private void ensureUserPermissions(Role role) {
        if (role == Role.NURSE || role == Role.DOCTOR) {
            // allowed; creation is still restricted by application-level authorization
        }
    }
}