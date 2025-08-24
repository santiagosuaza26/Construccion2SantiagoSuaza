package app.domain.repository;

import app.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findById(String id);

    Optional<User> findByUserName(String userName);

    List<User> findAll();

    void deleteById(String id);
}
