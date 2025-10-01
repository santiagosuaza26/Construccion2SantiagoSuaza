package app.infrastructure.adapter.mapper;

import org.springframework.stereotype.Component;

import app.domain.model.Credentials;
import app.domain.model.Role;
import app.domain.model.User;
import app.infrastructure.adapter.jpa.entity.CredentialsEntity;
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
        entity.setRole(user.getRole());

        if (user.getCredentials() != null) {
            // Crear nueva entidad CredentialsEntity con los datos del dominio
            CredentialsEntity credentialsEntity = new CredentialsEntity();
            credentialsEntity.setUsername(user.getCredentials().getUsername());
            credentialsEntity.setPassword(user.getCredentials().getPassword());
            // Nota: En una implementación real, aquí iría la relación @OneToOne
            // entity.setCredentialsEntity(credentialsEntity);
        }

        return entity;
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        // Crear objeto Role desde el enum
        Role role = entity.getRole();

        // Crear objeto Credentials desde los campos de la entidad
        // Nota: En una implementación real, aquí se obtendría de la relación @OneToOne
        Credentials credentials = new Credentials(entity.getUsername(), entity.getPassword());

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