package app.clinic.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.clinic.domain.model.valueobject.Role;

class RoleBasedAccessServiceTest {

    private RoleBasedAccessService roleBasedAccessService;

    @BeforeEach
    void setUp() {
        roleBasedAccessService = new RoleBasedAccessService();
    }

    @Test
    void shouldAllowHRToAccessUserData() {
        // Given
        Role role = Role.RECURSOS_HUMANOS;
        String resource = "user";

        // When & Then
        assertDoesNotThrow(() -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldDenyHRToAccessPatientData() {
        // Given
        Role role = Role.RECURSOS_HUMANOS;
        String resource = "patient";

        // When & Then
        assertThrows(IllegalAccessError.class, () -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldAllowAdminToAccessPatientData() {
        // Given
        Role role = Role.PERSONAL_ADMINISTRATIVO;
        String resource = "patient";

        // When & Then
        assertDoesNotThrow(() -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldDenyAdminToAccessMedicalRecords() {
        // Given
        Role role = Role.PERSONAL_ADMINISTRATIVO;
        String resource = "medicalRecord";

        // When & Then
        assertThrows(IllegalAccessError.class, () -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldAllowDoctorFullAccess() {
        // Given
        Role role = Role.MEDICO;
        String resource = "patient";

        // When & Then
        assertDoesNotThrow(() -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldThrowExceptionForUnknownRole() {
        // Given
        Role role = null; // Assuming null for unknown

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> roleBasedAccessService.checkAccess(role, "patient"));
    }
}