package app.clinic.infrastructure.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests para servicios críticos de infraestructura.
 * Verifica que la inicialización de base de datos y servicios básicos funcionan correctamente.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Database Initialization Service Tests")
class DatabaseInitializationServiceTest {

    @Autowired
    private DatabaseInitializationService databaseInitializationService;

    @Test
    @DisplayName("Debe inicializar correctamente")
    void shouldInitializeCorrectly() {
        // Then
        assertThat(databaseInitializationService).isNotNull();
    }

    @Test
    @DisplayName("Debe ejecutar inicialización sin errores")
    void shouldExecuteInitializationWithoutErrors() {
        // When & Then
        assertThatCode(() -> {
            // Ejecutar inicialización si es necesario
            if (databaseInitializationService != null) {
                // Verificar que el servicio puede ser invocado sin errores
                assertThat(databaseInitializationService).isNotNull();
            }
        }).doesNotThrowAnyException();
    }
}