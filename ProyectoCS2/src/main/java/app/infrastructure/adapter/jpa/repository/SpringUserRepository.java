package app.infrastructure.adapter.jpa.repository;

import app.infrastructure.adapter.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringUserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByUsername(String username);
}