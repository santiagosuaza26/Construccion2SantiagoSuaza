package app.adapter.out.persistence;

import app.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataUserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserName(String userName);
}
