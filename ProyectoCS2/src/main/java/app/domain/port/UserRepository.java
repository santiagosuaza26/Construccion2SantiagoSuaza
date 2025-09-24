package app.domain.port;

import java.util.List;
import java.util.Optional;

import app.domain.model.User;

public interface UserRepository {
    Optional<User> findByIdCard(String idCard);
    Optional<User> findByUsername(String username);
    User save(User user);
    void deleteByIdCard(String idCard);
    boolean existsByUsername(String username);
    boolean existsByIdCard(String idCard);
    List<User> findAll();
}