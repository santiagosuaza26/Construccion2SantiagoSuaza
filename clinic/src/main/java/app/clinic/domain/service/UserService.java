package app.clinic.domain.service;

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

public class UserService {
    private final UserRepository userRepository;
    private final UserValidationService validationService;
    private final RoleBasedAccessService roleBasedAccessService;

    public UserService(UserRepository userRepository, UserValidationService validationService, RoleBasedAccessService roleBasedAccessService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public User createUser(String fullName, String identificationNumber, String email, String phone, String dateOfBirth, String address, String role, String username, String password) {
        // Validar que el rol sea válido
        try {
            Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol inválido: " + role);
        }

        Id id = new Id(identificationNumber);
        if (userRepository.existsByIdentificationNumber(id)) {
            throw new IllegalArgumentException("Ya existe un usuario con este número de identificación");
        }

        // Validar unicidad de cédula (única en toda la aplicación)
        if (userRepository.existsByIdentificationNumber(id)) {
            throw new IllegalArgumentException("La cédula debe ser única en toda la aplicación");
        }

        Credentials credentials = new Credentials(new Username(username), new Password(password));
        // Validar unicidad de credenciales usando el servicio de dominio
        validationService.validateCredentialsUniqueness(credentials);

        User user = new User(credentials, fullName, id, new Email(email), new Phone(phone), new DateOfBirth(dateOfBirth), new Address(address), Role.valueOf(role));

        // Solo RRHH puede crear usuarios
        roleBasedAccessService.checkAccess(Role.RECURSOS_HUMANOS, "user");

        userRepository.save(user);
        return user;
    }

    public void updateUser(String identificationNumber, String fullName, String email, String phone, String dateOfBirth, String address, String role) {
        Id id = new Id(identificationNumber);
        User user = userRepository.findByIdentificationNumber(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        // For simplicity, create a new user with updated fields; in practice, use builder or update method
        Credentials credentials = user.getCredentials();
        User updatedUser = new User(credentials, fullName, id, new Email(email), new Phone(phone), new DateOfBirth(dateOfBirth), new Address(address), Role.valueOf(role));
        userRepository.save(updatedUser);
    }

    public void deleteUser(String identificationNumber) {
        Id id = new Id(identificationNumber);
        if (!userRepository.existsByIdentificationNumber(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteByIdentificationNumber(id);
    }

    public User findUserById(String identificationNumber) {
        Id id = new Id(identificationNumber);
        return userRepository.findByIdentificationNumber(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(new Username(username)).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }
}