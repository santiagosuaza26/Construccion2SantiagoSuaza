package app.clinic.application.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import app.clinic.application.dto.user.CreateUserDTO;
import app.clinic.application.dto.user.UpdateUserDTO;
import app.clinic.application.dto.user.UserDTO;
import app.clinic.domain.model.User;
import app.clinic.domain.model.UserAddress;
import app.clinic.domain.model.UserBirthDate;
import app.clinic.domain.model.UserCedula;
import app.clinic.domain.model.UserEmail;
import app.clinic.domain.model.UserFullName;
import app.clinic.domain.model.UserPassword;
import app.clinic.domain.model.UserPhoneNumber;
import app.clinic.domain.model.UserRole;
import app.clinic.domain.model.UserUsername;

/**
 * Mapper class for converting between User domain entities and DTOs.
 * Handles bidirectional conversion between domain objects and data transfer objects.
 */
public class UserMapper {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Converts a CreateUserDTO to a User domain entity.
     */
    public static User toDomainEntity(CreateUserDTO dto) {
        // Split full name into first names and last names
        String[] nameParts = dto.getFullName().trim().split("\\s+", 2);
        String firstNames = nameParts[0];
        String lastNames = nameParts.length > 1 ? nameParts[1] : "";

        return User.of(
            UserCedula.of(dto.getCedula()),
            UserUsername.of(dto.getUsername()),
            UserPassword.of(dto.getPassword()),
            UserFullName.of(firstNames, lastNames),
            UserBirthDate.of(parseDate(dto.getBirthDate())),
            UserAddress.of(dto.getAddress()),
            UserPhoneNumber.of(dto.getPhoneNumber()),
            UserEmail.of(dto.getEmail()),
            UserRole.valueOf(dto.getRole().toUpperCase())
        );
    }

    /**
     * Converts a User domain entity to a UserDTO.
     */
    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setCedula(user.getCedula().getValue());
        dto.setUsername(user.getUsername().getValue());
        dto.setFullName(user.getFullName().getFullName());
        dto.setBirthDate(user.getBirthDate().getValue().format(DATE_FORMAT));
        dto.setAddress(user.getAddress().getValue());
        dto.setPhoneNumber(user.getPhoneNumber().getValue());
        dto.setEmail(user.getEmail().getValue());
        dto.setRole(user.getRole().name());
        dto.setActive(user.isActive());
        dto.setAge(user.getAge());
        return dto;
    }

    /**
     * Updates an existing User entity with data from UpdateUserDTO.
     * Creates a new User instance with updated data while preserving the original cedula and username.
     */
    public static User updateEntity(User existingUser, UpdateUserDTO dto) {
        // Split full name into first names and last names if provided
        String[] nameParts = dto.getFullName() != null ? dto.getFullName().trim().split("\\s+", 2) : null;
        String firstNames = nameParts != null ? nameParts[0] : existingUser.getFullName().getFirstNames();
        String lastNames = nameParts != null && nameParts.length > 1 ? nameParts[1] : existingUser.getFullName().getLastNames();

        return User.of(
            existingUser.getCedula(), // Preserve original cedula
            existingUser.getUsername(), // Preserve original username
            existingUser.getPassword(), // Password should be updated separately if needed
            dto.getFullName() != null ? UserFullName.of(firstNames, lastNames) : existingUser.getFullName(),
            dto.getBirthDate() != null ? UserBirthDate.of(parseDate(dto.getBirthDate())) : existingUser.getBirthDate(),
            dto.getAddress() != null ? UserAddress.of(dto.getAddress()) : existingUser.getAddress(),
            dto.getPhoneNumber() != null ? UserPhoneNumber.of(dto.getPhoneNumber()) : existingUser.getPhoneNumber(),
            dto.getEmail() != null ? UserEmail.of(dto.getEmail()) : existingUser.getEmail(),
            dto.getRole() != null ? UserRole.valueOf(dto.getRole().toUpperCase()) : existingUser.getRole(),
            existingUser.isActive() // Preserve active status
        );
    }

    /**
     * Helper method to parse date string in DD/MM/YYYY format to LocalDate.
     */
    private static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMAT);
    }
}