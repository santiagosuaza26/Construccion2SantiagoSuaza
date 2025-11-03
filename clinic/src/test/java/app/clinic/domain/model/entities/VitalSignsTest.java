package app.clinic.domain.model.entities;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class VitalSignsTest {

    private static final String VALID_PATIENT_ID = "123456789";
    private static final LocalDateTime VALID_DATE_TIME = LocalDateTime.now();
    private static final String VALID_BLOOD_PRESSURE = "120/80";
    private static final double VALID_TEMPERATURE = 36.5;
    private static final int VALID_PULSE = 70;
    private static final int VALID_OXYGEN_LEVEL = 98;

    @Test
    void shouldCreateVitalSignsWithValidData() {
        // Given
        String patientId = VALID_PATIENT_ID;
        LocalDateTime dateTime = VALID_DATE_TIME;
        String bloodPressure = VALID_BLOOD_PRESSURE;
        double temperature = VALID_TEMPERATURE;
        int pulse = VALID_PULSE;
        int oxygenLevel = VALID_OXYGEN_LEVEL;

        // When
        VitalSigns vitalSigns = new VitalSigns(patientId, dateTime, bloodPressure, temperature, pulse, oxygenLevel);

        // Then
        assertEquals(patientId, vitalSigns.getPatientIdentificationNumber(), "El ID del paciente debería coincidir");
        assertEquals(dateTime, vitalSigns.getDateTime(), "La fecha y hora deberían coincidir");
        assertEquals(bloodPressure, vitalSigns.getBloodPressure(), "La presión arterial debería coincidir");
        assertEquals(temperature, vitalSigns.getTemperature(), "La temperatura debería coincidir");
        assertEquals(pulse, vitalSigns.getPulse(), "El pulso debería coincidir");
        assertEquals(oxygenLevel, vitalSigns.getOxygenLevel(), "El nivel de oxígeno debería coincidir");
    }

    @Test
    void shouldCreateVitalSignsWithObservations() {
        // Given
        String observations = "Paciente estable";

        // When
        VitalSigns vitalSigns = new VitalSigns(VALID_PATIENT_ID, VALID_DATE_TIME, VALID_BLOOD_PRESSURE, VALID_TEMPERATURE, VALID_PULSE, VALID_OXYGEN_LEVEL, observations);

        // Then
        assertEquals(observations, vitalSigns.getObservations(), "Las observaciones deberían coincidir");
    }

    @Test
    void shouldThrowExceptionForNullPatientId() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> new VitalSigns(null, VALID_DATE_TIME, VALID_BLOOD_PRESSURE, VALID_TEMPERATURE, VALID_PULSE, VALID_OXYGEN_LEVEL),
            "Debería rechazar ID de paciente null");
    }

    @Test
    void shouldThrowExceptionForEmptyPatientId() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> new VitalSigns("", VALID_DATE_TIME, VALID_BLOOD_PRESSURE, VALID_TEMPERATURE, VALID_PULSE, VALID_OXYGEN_LEVEL),
            "Debería rechazar ID de paciente vacío");
    }

    @Test
    void shouldThrowExceptionForBlankPatientId() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> new VitalSigns("   ", VALID_DATE_TIME, VALID_BLOOD_PRESSURE, VALID_TEMPERATURE, VALID_PULSE, VALID_OXYGEN_LEVEL),
            "Debería rechazar ID de paciente solo espacios");
    }

    @Test
    void shouldThrowExceptionForNullDateTime() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> new VitalSigns(VALID_PATIENT_ID, null, VALID_BLOOD_PRESSURE, VALID_TEMPERATURE, VALID_PULSE, VALID_OXYGEN_LEVEL),
            "Debería rechazar fecha y hora null");
    }

    @Test
    void shouldThrowExceptionForNullBloodPressure() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> new VitalSigns(VALID_PATIENT_ID, VALID_DATE_TIME, null, VALID_TEMPERATURE, VALID_PULSE, VALID_OXYGEN_LEVEL),
            "Debería rechazar presión arterial null");
    }

    @Test
    void shouldThrowExceptionForEmptyBloodPressure() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> new VitalSigns(VALID_PATIENT_ID, VALID_DATE_TIME, "", VALID_TEMPERATURE, VALID_PULSE, VALID_OXYGEN_LEVEL),
            "Debería rechazar presión arterial vacía");
    }

    @Test
    void shouldThrowExceptionForBlankBloodPressure() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> new VitalSigns(VALID_PATIENT_ID, VALID_DATE_TIME, "   ", VALID_TEMPERATURE, VALID_PULSE, VALID_OXYGEN_LEVEL),
            "Debería rechazar presión arterial solo espacios");
    }

    @ParameterizedTest
    @ValueSource(doubles = {29.9, 45.1, -10.0, 100.0})
    void shouldThrowExceptionForInvalidTemperature(double invalidTemperature) {
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> new VitalSigns(VALID_PATIENT_ID, VALID_DATE_TIME, VALID_BLOOD_PRESSURE, invalidTemperature, VALID_PULSE, VALID_OXYGEN_LEVEL),
            "Debería rechazar temperatura fuera del rango 30.0-45.0");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 301, -100, 500})
    void shouldThrowExceptionForInvalidPulse(int invalidPulse) {
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> new VitalSigns(VALID_PATIENT_ID, VALID_DATE_TIME, VALID_BLOOD_PRESSURE, VALID_TEMPERATURE, invalidPulse, VALID_OXYGEN_LEVEL),
            "Debería rechazar pulso fuera del rango 0-300");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 101, -50, 200})
    void shouldThrowExceptionForInvalidOxygenLevel(int invalidOxygenLevel) {
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> new VitalSigns(VALID_PATIENT_ID, VALID_DATE_TIME, VALID_BLOOD_PRESSURE, VALID_TEMPERATURE, VALID_PULSE, invalidOxygenLevel),
            "Debería rechazar nivel de oxígeno fuera del rango 0-100");
    }

    @ParameterizedTest
    @MethodSource("provideBoundaryValues")
    void shouldAcceptBoundaryValues(double temperature, int pulse, int oxygenLevel) {
        // When
        VitalSigns vitalSigns = new VitalSigns(VALID_PATIENT_ID, VALID_DATE_TIME, VALID_BLOOD_PRESSURE, temperature, pulse, oxygenLevel);

        // Then
        assertEquals(temperature, vitalSigns.getTemperature(), "Debería aceptar temperatura en límite");
        assertEquals(pulse, vitalSigns.getPulse(), "Debería aceptar pulso en límite");
        assertEquals(oxygenLevel, vitalSigns.getOxygenLevel(), "Debería aceptar oxígeno en límite");
    }

    private static Stream<Arguments> provideBoundaryValues() {
        return Stream.of(
            Arguments.of(30.0, 0, 0),     // Valores mínimos
            Arguments.of(45.0, 300, 100)  // Valores máximos
        );
    }

    @Test
    void shouldHandleNullObservationsGracefully() {
        // When
        VitalSigns vitalSigns = new VitalSigns(VALID_PATIENT_ID, VALID_DATE_TIME, VALID_BLOOD_PRESSURE, VALID_TEMPERATURE, VALID_PULSE, VALID_OXYGEN_LEVEL, null);

        // Then
        assertEquals("", vitalSigns.getObservations(), "Debería convertir observaciones null a cadena vacía");
    }

    @Test
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // Given
        VitalSigns vs1 = new VitalSigns(VALID_PATIENT_ID, VALID_DATE_TIME, VALID_BLOOD_PRESSURE, VALID_TEMPERATURE, VALID_PULSE, VALID_OXYGEN_LEVEL);
        VitalSigns vs2 = new VitalSigns(VALID_PATIENT_ID, VALID_DATE_TIME, "130/85", 37.0, 75, 95); // Diferentes valores pero mismo ID y fecha
        // VitalSigns vs3 = new VitalSigns("987654321", VALID_DATE_TIME, VALID_BLOOD_PRESSURE, VALID_TEMPERATURE, VALID_PULSE, VALID_OXYGEN_LEVEL);

        // Then
        assertEquals(vs1, vs2, "Objetos con mismo ID y fecha deberían ser iguales");
        assertEquals(vs1.hashCode(), vs2.hashCode(), "Hash codes deberían coincidir para objetos iguales");
        // vs1 no debería ser igual a vs3 porque tienen diferente ID
    }

    @Test
    void shouldProvideMeaningfulToString() {
        // Given
        VitalSigns vitalSigns = new VitalSigns(VALID_PATIENT_ID, VALID_DATE_TIME, VALID_BLOOD_PRESSURE, VALID_TEMPERATURE, VALID_PULSE, VALID_OXYGEN_LEVEL);

        // When
        String toString = vitalSigns.toString();

        // Then
        assertEquals("VitalSigns for " + VALID_PATIENT_ID + " at " + VALID_DATE_TIME, toString, "toString debería ser descriptivo");
    }
}