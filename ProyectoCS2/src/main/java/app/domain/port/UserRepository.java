package app.domain.port;

import java.util.Optional;

import app.domain.model.User;

public interface UserRepository {
    Optional<User> findByIdCard(String idCard);
    User save(User user);
    void deleteByIdCard(String idCard);
    boolean existsByUsername(String username);
    boolean existsByIdCard(String idCard);
}
