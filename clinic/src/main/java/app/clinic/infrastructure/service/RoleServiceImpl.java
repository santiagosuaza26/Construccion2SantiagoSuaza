package app.clinic.infrastructure.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.valueobject.Role;

@Service
public class RoleServiceImpl {

    public List<Role> getAllRoles() {
        return Arrays.asList(Role.values());
    }

    public Role getRoleByName(String roleName) {
        try {
            return Role.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol no encontrado: " + roleName);
        }
    }

    public boolean isValidRole(String roleName) {
        try {
            Role.valueOf(roleName.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}