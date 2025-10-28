package app.clinic.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.entities.User;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Username;

public interface UserRepository {
    void save(User user);
    Optional<User> findByIdentificationNumber(Id identificationNumber);
    Optional<User> findByUsername(Username username);
    List<User> findAll();
    boolean existsByIdentificationNumber(Id identificationNumber);
    void deleteByIdentificationNumber(Id identificationNumber);
}