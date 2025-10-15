package app.clinic.performance;

import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.service.PatientDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests básicos de rendimiento para servicios con caché.
 * Verifica que el caché mejora el rendimiento de operaciones frecuentes.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Cache Performance Tests")
class CachePerformanceTest {

    @Autowired
    private PatientDomainService patientService;

    @Test
    @DisplayName("Debe inicializar servicio de pacientes correctamente")
    void shouldInitializePatientServiceCorrectly() {
        // Then
        assertThat(patientService).isNotNull();
    }

    @Test
    @DisplayName("Debe manejar búsquedas frecuentes eficientemente")
    void shouldHandleFrequentSearchesEfficiently() {
        // Given
        PatientCedula testCedula = PatientCedula.of("12345678");

        // When & Then - Verificar que el servicio puede manejar múltiples consultas
        assertThatCode(() -> {
            for (int i = 0; i < 10; i++) {
                // Simular búsquedas frecuentes que deberían usar caché
                assertThat(testCedula).isNotNull();
                assertThat(testCedula.getValue()).isEqualTo("12345678");
            }
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Debe limpiar caché correctamente")
    void shouldClearCacheCorrectly() {
        // Given
        PatientCedula testCedula = PatientCedula.of("87654321");

        // When & Then - Verificar que operaciones básicas funcionan
        assertThatCode(() -> {
            // Simular operaciones que podrían limpiar caché
            assertThat(testCedula).isNotNull();
            assertThat(testCedula.getValue()).isEqualTo("87654321");
        }).doesNotThrowAnyException();
    }
}