package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.User;
import app.clinic.domain.model.UserCedula;
import app.clinic.domain.model.UserRole;
import app.clinic.domain.model.UserUsername;
import app.clinic.infrastructure.entity.UserEntity;

/**
 * Service for mapping between domain objects and JPA entities.
 * Provides utility methods for converting between layers.
 */
@Service
public class DomainModelMapperService {

    /**
     * Maps a UserEntity to a User domain object.
     */
    public User toDomainUser(UserEntity entity) {
        return User.of(
                UserCedula.of(entity.getCedula()),
                UserUsername.of(entity.getUsername()),
                app.clinic.domain.model.UserPassword.ofHashed(entity.getPassword()),
                app.clinic.domain.model.UserFullName.of(entity.getFirstNames(), entity.getLastNames()),
                app.clinic.domain.model.UserBirthDate.of(entity.getBirthDate()),
                app.clinic.domain.model.UserAddress.of(entity.getAddress()),
                app.clinic.domain.model.UserPhoneNumber.of(entity.getPhoneNumber()),
                app.clinic.domain.model.UserEmail.of(entity.getEmail()),
                toDomainRole(entity.getRole()),
                entity.isActive()
        );
    }

    /**
     * Maps a User domain object to a UserEntity.
     */
    public UserEntity toEntityUser(User user) {
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