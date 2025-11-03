package app.clinic.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.clinic.domain.model.DomainException;
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
        String resource = "USER";

        // When & Then
        assertDoesNotThrow(() -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldDenyHRToAccessPatientData() {
        // Given
        Role role = Role.RECURSOS_HUMANOS;
        String resource = "PATIENT";

        // When & Then
        assertThrows(DomainException.class, () -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldAllowAdminToAccessPatientData() {
        // Given
        Role role = Role.PERSONAL_ADMINISTRATIVO;
        String resource = "PATIENT";

        // When & Then
        assertDoesNotThrow(() -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldDenyAdminToAccessFullMedicalRecords() {
        // Given
        Role role = Role.PERSONAL_ADMINISTRATIVO;
        String resource = "FULL_MEDICAL_RECORD";

        // When & Then
        assertThrows(DomainException.class, () -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldAllowDoctorFullAccess() {
        // Given
        Role role = Role.MEDICO;
        String resource = "PATIENT";

        // When & Then
        assertDoesNotThrow(() -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldThrowExceptionForNullRole() {
        // Given
        Role role = null;

        // When & Then
        assertThrows(DomainException.class, () -> roleBasedAccessService.checkAccess(role, "PATIENT"));
    }

    @Test
    void shouldThrowExceptionForNullResource() {
        // Given
        Role role = Role.MEDICO;
        String resource = null;

        // When & Then
        assertThrows(DomainException.class, () -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldThrowExceptionForEmptyResource() {
        // Given
        Role role = Role.MEDICO;
        String resource = "";

        // When & Then
        assertThrows(DomainException.class, () -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldThrowExceptionForUnknownResource() {
        // Given
        Role role = Role.MEDICO;
        String resource = "UNKNOWN_RESOURCE";

        // When & Then
        assertThrows(DomainException.class, () -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldAllowNurseToAccessMedicalRecord() {
        // Given
        Role role = Role.ENFERMERA;
        String resource = "MEDICAL_RECORD";

        // When & Then
        assertDoesNotThrow(() -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldDenyNurseToAccessFullMedicalRecord() {
        // Given
        Role role = Role.ENFERMERA;
        String resource = "FULL_MEDICAL_RECORD";

        // When & Then
        assertThrows(DomainException.class, () -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldAllowSupportToAccessInventory() {
        // Given
        Role role = Role.SOPORTE_DE_INFORMACION;
        String resource = "INVENTORY";

        // When & Then
        assertDoesNotThrow(() -> roleBasedAccessService.checkAccess(role, resource));
    }

    @Test
    void shouldDenySupportToAccessPatientData() {
        // Given
        Role role = Role.SOPORTE_DE_INFORMACION;
        String resource = "PATIENT";

        // When & Then
        assertThrows(DomainException.class, () -> roleBasedAccessService.checkAccess(role, resource));
    }
}