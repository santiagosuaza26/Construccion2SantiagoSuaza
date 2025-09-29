package app.infrastructure.adapter.mapper;

import org.springframework.stereotype.Component;

import app.domain.model.Credentials;
import app.domain.model.Role;
import app.domain.model.User;
import app.infrastructure.adapter.jpa.entity.UserEntity;

@Component("infrastructureUserMapper")
public class UserMapper {

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setIdCard(user.getIdCard());
        entity.setFullName(user.getFullName());
        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
        entity.setBirthDate(user.getBirthDate());
        entity.setAddress(user.getAddress());

        if (user.getCredentials() != null) {
            // TODO: Implementar conversión de Credentials a CredentialsEntity
            // entity.setUsername(user.getCredentials().getUsername());
        }

        return entity;
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        // TODO: Implementar conversión de RoleEntity a Role y CredentialsEntity a Credentials
        Role role = Role.ADMINISTRATIVE; // Valor por defecto temporal
        Credentials credentials = new Credentials("temp", "TempPass123!"); // Valor por defecto temporal

        return new User(
            entity.getFullName(),
            entity.getIdCard(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getBirthDate(),
            entity.getAddress(),
            role,
            credentials
        );
    }
}