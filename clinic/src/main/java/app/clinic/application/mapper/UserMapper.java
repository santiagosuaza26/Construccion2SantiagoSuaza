package app.clinic.application.mapper;

import app.clinic.domain.model.entities.User;
import app.clinic.infrastructure.dto.UserDTO;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getIdentificationNumber().getValue());
        dto.setFullName(user.getFullName());
        dto.setDateOfBirth(user.getDateOfBirth().toString());
        dto.setEmail(user.getEmail().getValue());
        dto.setPhone(user.getPhone().getValue());
        dto.setAddress(user.getAddress().getValue());
        dto.setRole(user.getRole().toString());
        dto.setUsername(user.getUsername().getValue());
        return dto;
    }
}