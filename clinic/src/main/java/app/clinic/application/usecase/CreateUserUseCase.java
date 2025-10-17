package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.application.dto.user.CreateUserDTO;
import app.clinic.application.dto.user.UserDTO;
import app.clinic.application.mapper.UserMapper;
import app.clinic.domain.model.User;
import app.clinic.domain.service.UserDomainService;

/**
 * Caso de uso para crear un nuevo usuario.
 * Coordina entre la capa de presentación y los servicios del dominio.
 */
@Service
public class CreateUserUseCase {

    private final UserDomainService userDomainService;

    public CreateUserUseCase(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    /**
     * Ejecuta el caso de uso de creación de usuario.
     *
     * @param request DTO con información del usuario a crear
     * @return DTO con información del usuario creado
     * @throws IllegalArgumentException si los datos del usuario son inválidos
     */
    public UserDTO execute(CreateUserDTO request) {
        // Validar datos de entrada
        validateRequest(request);

        // Mapear DTO a entidad del dominio
        User user = UserMapper.toDomainEntity(request);

        // Ejecutar lógica del dominio (independiente)
        User createdUser = userDomainService.createUser(user);

        // Mapear entidad del dominio a DTO de respuesta
        return UserMapper.toDTO(createdUser);
    }

    /**
     * Valida los datos de la solicitud de creación.
     */
    private void validateRequest(CreateUserDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("User creation request cannot be null");
        }

        if (request.getCedula() == null || request.getCedula().trim().isEmpty()) {
            throw new IllegalArgumentException("User cedula is required");
        }

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }

        if (request.getRole() == null || request.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }
    }
}