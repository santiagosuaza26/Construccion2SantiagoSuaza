package app.clinic.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.User;
import app.clinic.domain.model.UserAddress;
import app.clinic.domain.model.UserBirthDate;
import app.clinic.domain.model.UserCedula;
import app.clinic.domain.model.UserEmail;
import app.clinic.domain.model.UserFullName;
import app.clinic.domain.model.UserId;
import app.clinic.domain.model.UserPassword;
import app.clinic.domain.model.UserPhoneNumber;
import app.clinic.domain.model.UserRole;
import app.clinic.domain.model.UserUsername;
import app.clinic.domain.port.UserRepository;
import app.clinic.infrastructure.entity.UserEntity;
import app.clinic.infrastructure.repository.UserJpaRepository;

/**
 * Adapter that implements the UserRepository port using JPA.
 * Converts between domain objects and JPA entities.
 */
@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity savedEntity = userJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UserId userId) {
        return userJpaRepository.findById(Long.valueOf(userId.getValue()))
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByCedula(UserCedula cedula) {
        return userJpaRepository.findByCedula(cedula.getValue())
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(UserUsername username) {
        return userJpaRepository.findByUsername(username.getValue())
                .map(this::toDomain);
    }

    @Override
    public List<User> findByRole(UserRole role) {
        return userJpaRepository.findByRole(toEntityRole(role))
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllActive() {
        return userJpaRepository.findByActiveTrue()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByCedula(UserCedula cedula) {
        return userJpaRepository.existsByCedula(cedula.getValue());
    }

    @Override
    public boolean existsByUsername(UserUsername username) {
        return userJpaRepository.existsByUsername(username.getValue());
    }

    @Override
    public void deleteById(UserId userId) {
        userJpaRepository.deleteById(Long.valueOf(userId.getValue()));
    }

    @Override
    public void deleteByCedula(UserCedula cedula) {
        userJpaRepository.findByCedula(cedula.getValue())
                .ifPresent(userJpaRepository::delete);
    }

    @Override
    public long count() {
        return userJpaRepository.count();
    }

    @Override
    public long countByRole(UserRole role) {
        return userJpaRepository.countByRole(toEntityRole(role));
    }

    // Métodos de conversión entre dominio y entidad

    private UserEntity toEntity(User user) {
        return new UserEntity(
                user.getCedula().getValue(),
                user.getUsername().getValue(),
                user.getPassword().getHashedValue(),
                user.getFullName().getFirstNames(),
                user.getFullName().getLastNames(),
                user.getBirthDate().getValue(),
                user.getAddress().getValue(),
                user.getPhoneNumber().getValue(),
                user.getEmail().getValue(),
                toEntityRole(user.getRole()),
                user.isActive()
        );
    }

    private User toDomain(UserEntity entity) {
        return User.of(
                UserCedula.of(entity.getCedula()),
                UserUsername.of(entity.getUsername()),
                UserPassword.ofHashed(entity.getPassword()),
                UserFullName.of(entity.getFirstNames(), entity.getLastNames()),
                UserBirthDate.of(entity.getBirthDate()),
                UserAddress.of(entity.getAddress()),
                UserPhoneNumber.of(entity.getPhoneNumber()),
                UserEmail.of(entity.getEmail()),
                toDomainRole(entity.getRole()),
                entity.isActive()
        );
    }

    private UserEntity.UserRole toEntityRole(UserRole role) {
        return switch (role) {
            case HUMAN_RESOURCES -> UserEntity.UserRole.HUMAN_RESOURCES;
            case ADMINISTRATIVE_STAFF -> UserEntity.UserRole.ADMINISTRATIVE_STAFF;
            case INFORMATION_SUPPORT -> UserEntity.UserRole.INFORMATION_SUPPORT;
            case NURSE -> UserEntity.UserRole.NURSE;
            case DOCTOR -> UserEntity.UserRole.DOCTOR;
        };
    }

    private UserRole toDomainRole(UserEntity.UserRole role) {
        return switch (role) {
            case HUMAN_RESOURCES -> UserRole.HUMAN_RESOURCES;
            case ADMINISTRATIVE_STAFF -> UserRole.ADMINISTRATIVE_STAFF;
            case INFORMATION_SUPPORT -> UserRole.INFORMATION_SUPPORT;
            case NURSE -> UserRole.NURSE;
            case DOCTOR -> UserRole.DOCTOR;
        };
    }
}