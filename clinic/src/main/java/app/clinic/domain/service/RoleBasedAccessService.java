package app.clinic.domain.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.valueobject.Role;

@Service
public class RoleBasedAccessService {

    public void checkAccess(Role userRole, String resource) {
        if (userRole == null) {
            throw new IllegalArgumentException("Unknown role");
        }

        switch (userRole) {
            case RECURSOS_HUMANOS:
                validateHumanResourcesAccess(resource);
                break;
            case PERSONAL_ADMINISTRATIVO:
                validateAdministrativeAccess(resource);
                break;
            case SOPORTE_DE_INFORMACION:
                validateSupportAccess(resource);
                break;
            case ENFERMERA:
                validateNursingAccess(resource);
                break;
            case MEDICO:
                validateMedicalAccess(resource);
                break;
            default:
                throw new IllegalArgumentException("Unknown role: " + userRole);
        }
    }

    private void validateHumanResourcesAccess(String resource) {
        // Recursos Humanos solo pueden gestionar usuarios
        if (!resource.equals("user")) {
            throw new IllegalAccessError("Human Resources can only manage users. Access denied to: " + resource);
        }
    }

    private void validateAdministrativeAccess(String resource) {
        // Personal Administrativo puede:
        // - Gestionar pacientes (registrar, actualizar info básica)
        // - Programar citas
        // - Gestionar facturación
        // - Gestionar seguros médicos
        // NO puede acceder a historia clínica completa
        if (resource.equals("fullMedicalRecord") || resource.equals("medicalRecord")) {
            throw new IllegalAccessError("Administrative staff cannot access medical records. Access denied to: " + resource);
        }
        // Validar recursos permitidos
        if (!isAdministrativeAllowedResource(resource)) {
            throw new IllegalAccessError("Administrative staff access denied to: " + resource);
        }
    }

    private void validateSupportAccess(String resource) {
        // Soporte de Información puede:
        // - Gestionar inventarios (medicamentos, procedimientos, ayudas diagnósticas)
        // - Mantener integridad de datos
        // NO puede acceder a datos de pacientes
        if (resource.equals("patient") || resource.equals("medicalRecord") || resource.equals("fullMedicalRecord")) {
            throw new IllegalAccessError("IT Support cannot access patient data. Access denied to: " + resource);
        }
        // Validar recursos permitidos
        if (!isSupportAllowedResource(resource)) {
            throw new IllegalAccessError("IT Support access denied to: " + resource);
        }
    }

    private void validateNursingAccess(String resource) {
        // Enfermeras pueden:
        // - Consultar información básica de pacientes
        // - Registrar signos vitales
        // - Administrar medicamentos y procedimientos según órdenes médicas
        // - Registrar observaciones
        // NO pueden acceder a historia clínica completa
        if (resource.equals("fullMedicalRecord")) {
            throw new IllegalAccessError("Nurses cannot access full medical records. Access denied to: " + resource);
        }
        // Validar recursos permitidos
        if (!isNursingAllowedResource(resource)) {
            throw new IllegalAccessError("Nursing staff access denied to: " + resource);
        }
    }

    private void validateMedicalAccess(String resource) {
        // Médicos tienen acceso completo a:
        // - Historia clínica completa
        // - Crear y gestionar órdenes médicas
        // - Gestionar registros médicos
        // Todas las operaciones están permitidas
    }

    private boolean isAdministrativeAllowedResource(String resource) {
        return resource.equals("patient") ||
               resource.equals("appointment") ||
               resource.equals("billing") ||
               resource.equals("insurance") ||
               resource.equals("user"); // Para ver usuarios si es necesario
    }

    private boolean isSupportAllowedResource(String resource) {
        return resource.equals("inventory") ||
               resource.equals("medication") ||
               resource.equals("procedure") ||
               resource.equals("diagnosticAid") ||
               resource.equals("dataIntegrity");
    }

    private boolean isNursingAllowedResource(String resource) {
        return resource.equals("patient") ||
               resource.equals("vitalSigns") ||
               resource.equals("medicationAdministration") ||
               resource.equals("procedureAdministration") ||
               resource.equals("medicalRecord") || // Acceso limitado
               resource.equals("order"); // Para ver órdenes asignadas
    }

    // Método adicional para validar operaciones específicas
    public void validateOrderCreation(Role userRole, String orderType) {
        if (userRole != Role.MEDICO) {
            throw new IllegalAccessError("Only doctors can create medical orders");
        }

        // Validar reglas específicas de órdenes médicas
        if ("diagnosticAid".equals(orderType)) {
            // Si es ayuda diagnóstica, no puede incluir medicamentos ni procedimientos
            // Esta validación se hace en el dominio Order
        }
        if ("postDiagnostic".equals(orderType)) {
            // Órdenes post-diagnóstico pueden incluir medicamentos y procedimientos
        }
    }

    public void validatePatientDataAccess(Role userRole, boolean isFullRecord) {
        if (userRole == Role.PERSONAL_ADMINISTRATIVO && isFullRecord) {
            throw new IllegalAccessError("Administrative staff cannot access full patient medical records");
        }
        if (userRole == Role.ENFERMERA && isFullRecord) {
            throw new IllegalAccessError("Nursing staff cannot access full patient medical records");
        }
        if (userRole == Role.SOPORTE_DE_INFORMACION) {
            throw new IllegalAccessError("IT Support cannot access patient data");
        }
        if (userRole == Role.RECURSOS_HUMANOS) {
            throw new IllegalAccessError("Human Resources cannot access patient data");
        }
    }
}