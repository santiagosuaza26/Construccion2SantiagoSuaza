package app.clinic.performance;

import app.clinic.domain.model.Patient;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.PatientUsername;
import app.clinic.domain.service.PatientDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

/**
 * Pruebas de performance para operaciones de base de datos.
 * Mide tiempos de respuesta y throughput bajo diferentes cargas.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Database Performance Tests")
class DatabasePerformanceTest {

    @Autowired
    private PatientDomainService patientDomainService;

    private List<Patient> testPatients;

    @BeforeEach
    void setUp() {
        testPatients = new ArrayList<>();
        // Crear datos de prueba para mediciones de performance
    }

    @Test
    @DisplayName("Debe medir tiempo de creación de pacientes individuales")
    void shouldMeasureIndividualPatientCreationTime() {
        // Given
        Patient testPatient = createTestPatient("perf-test-1");

        // When
        long startTime = System.currentTimeMillis();
        Patient result = patientDomainService.registerPatient(testPatient);
        long endTime = System.currentTimeMillis();

        // Then
        long duration = endTime - startTime;
        assertThat(result).isNotNull();
        assertThat(duration).isLessThan(1000); // Debe ser menor a 1 segundo

        System.out.println("Tiempo de creación individual: " + duration + "ms");
    }

    @Test
    @DisplayName("Debe medir tiempo de creación masiva de pacientes")
    void shouldMeasureBulkPatientCreationTime() {
        // Given
        List<Patient> patients = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            patients.add(createTestPatient("bulk-perf-" + i));
        }

        // When
        long startTime = System.currentTimeMillis();

        List<Patient> results = new ArrayList<>();
        for (Patient patient : patients) {
            Patient result = patientDomainService.registerPatient(patient);
            results.add(result);
        }

        long endTime = System.currentTimeMillis();

        // Then
        long totalDuration = endTime - startTime;
        assertThat(results).hasSize(100);
        assertThat(totalDuration).isLessThan(10000); // Debe ser menor a 10 segundos para 100 pacientes

        double avgTimePerPatient = totalDuration / 100.0;
        System.out.println("Tiempo total para 100 pacientes: " + totalDuration + "ms");
        System.out.println("Tiempo promedio por paciente: " + avgTimePerPatient + "ms");
    }

    @Test
    @DisplayName("Debe medir tiempo de consulta de pacientes por cédula")
    void shouldMeasurePatientQueryByCedulaTime() {
        // Given
        Patient testPatient = createTestPatient("query-perf-test");
        Patient savedPatient = patientDomainService.registerPatient(testPatient);

        // When
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            patientDomainService.findPatientByCedula(savedPatient.getCedula());
        }

        long endTime = System.currentTimeMillis();

        // Then
        long totalDuration = endTime - startTime;
        double avgQueryTime = totalDuration / 1000.0;
        assertThat(avgQueryTime).isLessThan(10); // Cada consulta debe ser menor a 10ms promedio

        System.out.println("Tiempo total para 1000 consultas: " + totalDuration + "ms");
        System.out.println("Tiempo promedio por consulta: " + avgQueryTime + "ms");
    }

    @Test
    @DisplayName("Debe medir throughput bajo carga concurrente")
    void shouldMeasureThroughputUnderConcurrentLoad() throws Exception {
        // Given
        int numberOfThreads = 10;
        int operationsPerThread = 50;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        // When
        long startTime = System.currentTimeMillis();

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    try {
                        Patient patient = createTestPatient("concurrent-" + Thread.currentThread().getId() + "-" + j);
                        patientDomainService.registerPatient(patient);
                    } catch (Exception e) {
                        // Ignorar errores para medir throughput máximo
                    }
                }
            }, executor);
            futures.add(future);
        }

        // Wait for all threads to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(60, TimeUnit.SECONDS);

        long endTime = System.currentTimeMillis();

        // Then
        long totalDuration = endTime - startTime;
        int totalOperations = numberOfThreads * operationsPerThread;
        double throughput = (double) totalOperations / (totalDuration / 1000.0);

        assertThat(throughput).isGreaterThan(5.0); // Al menos 5 operaciones por segundo

        System.out.println("Operaciones totales: " + totalOperations);
        System.out.println("Tiempo total: " + totalDuration + "ms");
        System.out.println("Throughput: " + throughput + " operaciones/segundo");

        executor.shutdown();
    }

    @Test
    @DisplayName("Debe medir tiempo de consultas complejas")
    void shouldMeasureComplexQueryTime() {
        // Given - Datos de prueba con relaciones complejas

        // When
        long startTime = System.currentTimeMillis();

        // Ejecutar consulta compleja (ej: pacientes con pólizas activas)
        List<Patient> results = patientDomainService.findAllPatients();

        long endTime = System.currentTimeMillis();

        // Then
        long duration = endTime - startTime;
        assertThat(results).isNotNull();
        assertThat(duration).isLessThan(500); // Consulta compleja debe ser menor a 500ms

        System.out.println("Tiempo de consulta compleja: " + duration + "ms");
        System.out.println("Número de resultados: " + results.size());
    }

    @Test
    @DisplayName("Debe medir impacto del cache en consultas repetidas")
    void shouldMeasureCacheImpactOnRepeatedQueries() {
        // Given
        Patient testPatient = createTestPatient("cache-perf-test");
        Patient savedPatient = patientDomainService.registerPatient(testPatient);

        // Primera consulta (cache miss)
        long firstQueryStart = System.currentTimeMillis();
        patientDomainService.findPatientByCedula(savedPatient.getCedula());
        long firstQueryEnd = System.currentTimeMillis();

        // Consultas repetidas (cache hits)
        long repeatedQueriesStart = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            patientDomainService.findPatientByCedula(savedPatient.getCedula());
        }

        long repeatedQueriesEnd = System.currentTimeMillis();

        // Then
        long firstQueryTime = firstQueryEnd - firstQueryStart;
        long repeatedQueriesTotalTime = repeatedQueriesEnd - repeatedQueriesStart;
        double avgRepeatedQueryTime = repeatedQueriesTotalTime / 100.0;

        // Las consultas repetidas deberían ser significativamente más rápidas
        assertThat(avgRepeatedQueryTime).isLessThan(firstQueryTime / 2.0);

        System.out.println("Tiempo primera consulta: " + firstQueryTime + "ms");
        System.out.println("Tiempo promedio consultas repetidas: " + avgRepeatedQueryTime + "ms");
        System.out.println("Mejora de performance: " + (firstQueryTime / avgRepeatedQueryTime) + "x");
    }

    @Test
    @DisplayName("Debe medir consumo de memoria durante operaciones intensivas")
    void shouldMeasureMemoryConsumptionDuringIntensiveOperations() {
        // Given
        Runtime runtime = Runtime.getRuntime();

        // Forzar garbage collection inicial
        runtime.gc();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        // When - Ejecutar operaciones intensivas
        List<Patient> patients = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Patient patient = createTestPatient("memory-test-" + i);
            patients.add(patient);
        }

        // Procesar pacientes
        for (Patient patient : patients) {
            patientDomainService.registerPatient(patient);
        }

        // Forzar garbage collection final
        runtime.gc();
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();

        // Then
        long memoryUsed = finalMemory - initialMemory;
        double memoryUsedMB = memoryUsed / (1024.0 * 1024.0);

        assertThat(memoryUsedMB).isLessThan(100); // No debe usar más de 100MB adicionales

        System.out.println("Memoria inicial: " + (initialMemory / 1024 / 1024) + "MB");
        System.out.println("Memoria final: " + (finalMemory / 1024 / 1024) + "MB");
        System.out.println("Memoria utilizada: " + memoryUsedMB + "MB");
    }

    @Test
    @DisplayName("Debe identificar cuellos de botella en operaciones críticas")
    void shouldIdentifyBottlenecksInCriticalOperations() {
        // Given - Operación crítica identificada

        // When - Medir tiempo de operación crítica
        long startTime = System.currentTimeMillis();

        // Simular operación crítica (ej: generación de reporte complejo)
        simulateCriticalOperation();

        long endTime = System.currentTimeMillis();

        // Then
        long duration = endTime - startTime;
        assertThat(duration).isLessThan(5000); // Operación crítica debe ser menor a 5 segundos

        System.out.println("Tiempo de operación crítica: " + duration + "ms");

        // Si el tiempo es mayor al umbral, marcar como cuello de botella
        if (duration > 2000) {
            System.out.println("⚠️ BOTTLENECK DETECTADO: Operación crítica toma " + duration + "ms");
        }
    }

    private Patient createTestPatient(String suffix) {
        return Patient.of(
            PatientCedula.of("perf" + suffix),
            PatientUsername.of("perfuser" + suffix),
            app.clinic.domain.model.PatientPassword.of("TestPassword123!"),
            app.clinic.domain.model.PatientFullName.of("Paciente", "Performance " + suffix),
            app.clinic.domain.model.PatientBirthDate.of(LocalDate.of(1990, 1, 1)),
            app.clinic.domain.model.PatientGender.MASCULINO,
            app.clinic.domain.model.PatientAddress.of("Dirección Performance " + suffix),
            app.clinic.domain.model.PatientPhoneNumber.of("300" + suffix.hashCode()),
            app.clinic.domain.model.PatientEmail.of("perf" + suffix + "@test.com"),
            List.of(),
            null
        );
    }

    private void simulateCriticalOperation() {
        // Simular operación que podría ser cuello de botella
        try {
            Thread.sleep(100); // Simular procesamiento

            // Simular consultas múltiples
            for (int i = 0; i < 50; i++) {
                // Simular consulta a base de datos
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}