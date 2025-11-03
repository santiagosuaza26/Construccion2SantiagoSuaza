package app.clinic.domain.service;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import app.clinic.domain.model.DomainException;
import app.clinic.domain.model.valueobject.Role;

public class RoleBasedAccessService {

    // Enum para recursos para mejor mantenibilidad y evitar errores de tipeo
    public enum Resource {
        USER,
        PATIENT,
        APPOINTMENT,
        BILLING,
        INSURANCE,
        INVENTORY,
        MEDICATION,
        PROCEDURE,
        DIAGNOSTIC_AID,
        DATA_INTEGRITY,
        VITAL_SIGNS,
        MEDICATION_ADMINISTRATION,
        PROCEDURE_ADMINISTRATION,
        MEDICAL_RECORD,
        FULL_MEDICAL_RECORD,
        ORDER
    }

    // Mapa de permisos por rol para mejor rendimiento y mantenibilidad
    private final Map<Role, Set<Resource>> rolePermissions;

    public RoleBasedAccessService() {
        rolePermissions = new EnumMap<>(Role.class);

        // Recursos Humanos: solo usuarios (NO pacientes, medicamentos, procedimientos)
        rolePermissions.put(Role.RECURSOS_HUMANOS, EnumSet.of(Resource.USER));

        // Personal Administrativo: pacientes, citas, facturación, seguros, usuarios
        rolePermissions.put(Role.PERSONAL_ADMINISTRATIVO, EnumSet.of(
            Resource.PATIENT, Resource.APPOINTMENT, Resource.BILLING,
            Resource.INSURANCE, Resource.USER
        ));

        // Soporte de Información: inventarios y datos técnicos (NO pacientes)
        rolePermissions.put(Role.SOPORTE_DE_INFORMACION, EnumSet.of(
            Resource.INVENTORY, Resource.MEDICATION, Resource.PROCEDURE,
            Resource.DIAGNOSTIC_AID, Resource.DATA_INTEGRITY
        ));

        // Enfermeras: acceso limitado a pacientes y registros médicos
        rolePermissions.put(Role.ENFERMERA, EnumSet.of(
            Resource.PATIENT, Resource.VITAL_SIGNS, Resource.MEDICATION_ADMINISTRATION,
            Resource.PROCEDURE_ADMINISTRATION, Resource.MEDICAL_RECORD, Resource.ORDER
        ));

        // Médicos: acceso completo
        rolePermissions.put(Role.MEDICO, EnumSet.allOf(Resource.class));
    }

    public void checkAccess(Role userRole, String resource) {
        validateParameters(userRole, resource);

        Resource resourceEnum = parseResource(resource);
        Set<Resource> allowedResources = rolePermissions.get(userRole);

        if (allowedResources == null || !allowedResources.contains(resourceEnum)) {
            throw new DomainException(
                String.format("Acceso denegado para el rol %s al recurso %s", userRole, resource)
            );
        }

        // Validaciones adicionales específicas
        if (resourceEnum == Resource.PATIENT && (userRole == Role.RECURSOS_HUMANOS || userRole == Role.SOPORTE_DE_INFORMACION)) {
            throw new DomainException("Recursos Humanos y Soporte de Información no pueden acceder a datos de pacientes");
        }
    }

    private void validateParameters(Role userRole, String resource) {
        if (userRole == null) {
            throw new DomainException("User role cannot be null");
        }
        if (resource == null || resource.trim().isEmpty()) {
            throw new DomainException("Resource cannot be null or empty");
        }
    }

    private Resource parseResource(String resource) {
        try {
            // Convertir string a enum (asumiendo que los recursos vienen en camelCase o UPPER_CASE)
            String normalized = resource.toUpperCase().replace("-", "_");
            return Resource.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            throw new DomainException("Unknown resource: " + resource);
        }
    }

    // Método adicional para validar operaciones específicas
    public void validateOrderCreation(Role userRole, String orderType) {
        if (userRole != Role.MEDICO) {
            throw new DomainException("Only doctors can create medical orders");
        }

        validateOrderType(orderType);
    }

    private void validateOrderType(String orderType) {
        if (orderType == null || orderType.trim().isEmpty()) {
            throw new DomainException("Order type cannot be null or empty");
        }

        // Validaciones específicas pueden agregarse aquí si es necesario
        // Por ahora, solo validamos que no sea vacío
    }

    public void validatePatientDataAccess(Role userRole, boolean isFullRecord) {
        if (userRole == null) {
            throw new DomainException("User role cannot be null");
        }

        // Restricción crítica: RRHH y Soporte no pueden acceder a datos de pacientes
        if (userRole == Role.SOPORTE_DE_INFORMACION || userRole == Role.RECURSOS_HUMANOS) {
            throw new DomainException(
                String.format("%s no puede acceder a información de pacientes", userRole)
            );
        }

        // Restricción adicional: Solo médicos pueden acceder a registros médicos completos
        if (isFullRecord && userRole != Role.MEDICO) {
            throw new DomainException(
                String.format("%s no puede acceder a registros médicos completos", userRole)
            );
        }

        if (isFullRecord) {
            if (userRole == Role.PERSONAL_ADMINISTRATIVO || userRole == Role.ENFERMERA) {
                throw new DomainException(
                    String.format("%s no puede acceder a registros médicos completos", userRole)
                );
            }
        } else {
            // Para acceso no completo, verificar permisos generales
            checkAccess(userRole, "patient");
        }
    }
}