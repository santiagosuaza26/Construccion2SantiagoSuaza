package app.infrastructure.adapter;

import app.domain.model.User;
import app.domain.port.UserRepository;
import app.infrastructure.adapter.entity.UserEntity;
import app.infrastructure.adapter.mapper.UserMapper;
import app.infrastructure.adapter.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findByIdCard(String idCard) {
        return userJpaRepository.findByIdCard(idCard)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByCredentials_Username(username)
                .map(userMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = userJpaRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteByIdCard(String idCard) {
        userJpaRepository.deleteByIdCard(idCard);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByCredentials_Username(username);
    }

    @Override
    public boolean existsByIdCard(String idCard) {
        return userJpaRepository.existsByIdCard(idCard);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }
}