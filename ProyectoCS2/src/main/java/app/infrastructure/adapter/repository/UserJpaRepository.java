package app.infrastructure.adapter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.infrastructure.adapter.jpa.entity.UserEntity;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByIdCard(String idCard);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByIdCard(String idCard);
    boolean existsByEmail(String email);
    List<UserEntity> findAll();
    void deleteByIdCard(String idCard);
}