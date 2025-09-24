package app.domain.factory;

import java.time.LocalDate;

import app.domain.model.Credentials;
import app.domain.model.Role;
import app.domain.model.User;

public class UserFactory {
    
    public User createUser(String fullName, String idCard, String email, 
                            String phone, LocalDate birthDate, String address, 
                            Role role, String username, String password) {
        
        // Validaciones específicas por rol
        validateRoleSpecificRequirements(role, email, phone);
        
        Credentials credentials = new Credentials(username, password);
        
        return new User(fullName, idCard, email, phone, birthDate, 
                        address, role, credentials);
    }
    
    private void validateRoleSpecificRequirements(Role role, String email, String phone) {
        switch (role) {
            case DOCTOR, NURSE -> {
                // Médicos y enfermeras requieren validaciones adicionales
                if (email == null || !email.contains("@")) {
                    throw new IllegalArgumentException("Medical staff requires valid email");
                }
            }
            case HUMAN_RESOURCES -> {
                // RRHH no puede ver información de pacientes (validado en servicios)
            }
            default -> {
                // Validaciones generales ya están en User
            }
        }
    }
}