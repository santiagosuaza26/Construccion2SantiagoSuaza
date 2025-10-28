package app.clinic.domain.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import app.clinic.domain.model.entities.VitalSigns;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;

class VitalSignsServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleBasedAccessService roleBasedAccessService;

    private VitalSignsService vitalSignsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vitalSignsService = new VitalSignsService(patientRepository, userRepository, roleBasedAccessService);
    }

    @Test
    void shouldRecordVitalSignsSuccessfully() {
        // Given
        String patientId = "123456789";
        String bloodPressure = "120/80";
        double temperature = 36.5;
        int pulse = 70;
        int oxygenLevel = 98;

        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(true);

        // When
        VitalSigns vitalSigns = vitalSignsService.recordVitalSigns(patientId, bloodPressure, temperature, pulse, oxygenLevel);

        // Then
        assertNotNull(vitalSigns);
        verify(patientRepository).saveVitalSigns(vitalSigns);
    }

    @Test
    void shouldThrowExceptionForNonExistentPatient() {
        // Given
        when(patientRepository.existsByIdentificationNumber(any(Id.class))).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> vitalSignsService.recordVitalSigns("123456789", "120/80", 36.5, 70, 98));
    }
}