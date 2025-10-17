package app.clinic.application.mapper;

import java.time.LocalDate;

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
 * Mapper para convertir entre DTOs de aplicación y entidades del dominio para usuarios.
 * Mantiene la separación entre capas de presentación y dominio.
 */
public class UserMapper {

    /**
     * Convierte un DTO de creación a entidad del dominio.
     */
    public static User toDomainEntity(CreateUserDTO dto) {
        // Parsear nombre completo (formato "Nombre Apellido")
        String[] nameParts = dto.getFullName().split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        return User.of(
            UserCedula.of(dto.getCedula()),
            UserUsername.of(dto.getUsername()),
            UserPassword.of(dto.getPassword()),
            UserFullName.of(firstName, lastName),
            UserBirthDate.of(LocalDate.parse(dto.getBirthDate())),
            UserAddress.of(dto.getAddress()),
            UserPhoneNumber.of(dto.getPhoneNumber()),
            UserEmail.of(dto.getEmail()),
            UserRole.valueOf(dto.getRole().toUpperCase())
        );
    }

    /**
     * Convierte una entidad del dominio a DTO de respuesta.
     */
    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setCedula(user.getCedula().getValue());
        dto.setUsername(user.getUsername().getValue());
        dto.setFullName(user.getFullName().getFullName());
        dto.setBirthDate(user.getBirthDate().getValue().toString());
        dto.setAddress(user.getAddress().getValue());
        dto.setPhoneNumber(user.getPhoneNumber().getValue());
        dto.setEmail(user.getEmail().getValue());
        dto.setRole(user.getRole().name());
        dto.setActive(user.isActive());
        dto.setAge(user.getAge());
        return dto;
    }

    /**
     * Convierte un DTO de actualización a entidad del dominio.
     */
    public static User toDomainEntityForUpdate(User existingUser, UpdateUserDTO updateDTO) {
        // Crear nuevo usuario con datos actualizados
        return User.of(
            existingUser.getCedula(), // Mantener cédula original
            existingUser.getUsername(), // Mantener username existente
            existingUser.getPassword(), // Mantener contraseña existente
            updateDTO.getFullName() != null ?
                UserFullName.of(updateDTO.getFullName().split(" ", 2)[0], updateDTO.getFullName().split(" ", 2)[1]) :
                existingUser.getFullName(),
            existingUser.getBirthDate(), // Mantener fecha de nacimiento
            UserAddress.of(updateDTO.getAddress() != null ? updateDTO.getAddress() : existingUser.getAddress().getValue()),
            UserPhoneNumber.of(updateDTO.getPhoneNumber() != null ? updateDTO.getPhoneNumber() : existingUser.getPhoneNumber().getValue()),
            UserEmail.of(updateDTO.getEmail() != null ? updateDTO.getEmail() : existingUser.getEmail().getValue()),
            updateDTO.getRole() != null ? UserRole.valueOf(updateDTO.getRole().toUpperCase()) : existingUser.getRole()
        );
    }
}