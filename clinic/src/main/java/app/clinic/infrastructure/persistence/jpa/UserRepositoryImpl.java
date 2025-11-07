package app.clinic.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.entities.User;
import app.clinic.domain.model.valueobject.Address;
import app.clinic.domain.model.valueobject.Credentials;
import app.clinic.domain.model.valueobject.DateOfBirth;
import app.clinic.domain.model.valueobject.Email;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Password;
import app.clinic.domain.model.valueobject.Phone;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.model.valueobject.Username;
import app.clinic.domain.repository.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(User user) {
        UserJpaEntity entity = new UserJpaEntity(
            user.getIdentificationNumber().getValue(),
            user.getFullName(),
            user.getEmail().getValue(),
            user.getPhone().getValue(),
            user.getDateOfBirth().toString(),
            user.getAddress().getValue(),
            user.getRole().toString(),
            user.getCredentials().getUsername().getValue(),
            user.getCredentials().getPassword().getValue()
        );
        jpaRepository.save(entity);
    }

    @Override
    public Optional<User> findByIdentificationNumber(Id identificationNumber) {
        return jpaRepository.findById(identificationNumber.getValue())
            .map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(Username username) {
        return jpaRepository.findByUsername(username.getValue())
            .map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByIdentificationNumber(Id identificationNumber) {
        return jpaRepository.existsByIdentificationNumber(identificationNumber.getValue());
    }

    @Override
    public void deleteByIdentificationNumber(Id identificationNumber) {
        jpaRepository.deleteById(identificationNumber.getValue());
    }

    private User toDomain(UserJpaEntity entity) {
        Credentials credentials = new Credentials(
            new Username(entity.getUsername()),
            new Password(entity.getPassword(), true) // true = password is hashed for database storage
        );

        return new User(
            credentials,
            entity.getFullName(),
            new Id(entity.getIdentificationNumber()),
            new Email(entity.getEmail()),
            new Phone(entity.getPhone()),
            new DateOfBirth(entity.getDateOfBirth()),
            new Address(entity.getAddress()),
            Role.valueOf(entity.getRole())
        );
    }
}