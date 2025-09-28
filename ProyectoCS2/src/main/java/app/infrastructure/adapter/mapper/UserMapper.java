package app.infrastructure.adapter.mapper;

import app.domain.model.User;
import app.infrastructure.adapter.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setIdCard(user.getIdCard());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
        entity.setActive(user.isActive());

        if (user.getCredentials() != null) {
            entity.setUsername(user.getCredentials().getUsername());
        }

        return entity;
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new User(
            entity.getIdCard(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getEmail(),
            entity.getPhone(),
            entity.isActive()
        );
    }
}