package app.clinic.domain.model.entities;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class VitalSignsTest {

    @Test
    void shouldCreateVitalSignsWithValidData() {
        // Given
        String patientId = "123456789";
        LocalDateTime dateTime = LocalDateTime.now();
        String bloodPressure = "120/80";
        double temperature = 36.5;
        int pulse = 70;
        int oxygenLevel = 98;

        // When
        VitalSigns vitalSigns = new VitalSigns(patientId, dateTime, bloodPressure, temperature, pulse, oxygenLevel);

        // Then
        assertEquals(patientId, vitalSigns.getPatientIdentificationNumber());
        assertEquals(dateTime, vitalSigns.getDateTime());
        assertEquals(bloodPressure, vitalSigns.getBloodPressure());
        assertEquals(temperature, vitalSigns.getTemperature());
        assertEquals(pulse, vitalSigns.getPulse());
        assertEquals(oxygenLevel, vitalSigns.getOxygenLevel());
    }

    @Test
    void shouldThrowExceptionForNullPatientId() {
        // Given
        String patientId = null;
        LocalDateTime dateTime = LocalDateTime.now();
        String bloodPressure = "120/80";
        double temperature = 36.5;
        int pulse = 70;
        int oxygenLevel = 98;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new VitalSigns(patientId, dateTime, bloodPressure, temperature, pulse, oxygenLevel));
    }

    @Test
    void shouldThrowExceptionForInvalidTemperature() {
        // Given
        String patientId = "123456789";
        LocalDateTime dateTime = LocalDateTime.now();
        String bloodPressure = "120/80";
        double temperature = 50.0; // Invalid
        int pulse = 70;
        int oxygenLevel = 98;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new VitalSigns(patientId, dateTime, bloodPressure, temperature, pulse, oxygenLevel));
    }

    @Test
    void shouldThrowExceptionForInvalidPulse() {
        // Given
        String patientId = "123456789";
        LocalDateTime dateTime = LocalDateTime.now();
        String bloodPressure = "120/80";
        double temperature = 36.5;
        int pulse = 400; // Invalid
        int oxygenLevel = 98;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new VitalSigns(patientId, dateTime, bloodPressure, temperature, pulse, oxygenLevel));
    }

    @Test
    void shouldThrowExceptionForInvalidOxygenLevel() {
        // Given
        String patientId = "123456789";
        LocalDateTime dateTime = LocalDateTime.now();
        String bloodPressure = "120/80";
        double temperature = 36.5;
        int pulse = 70;
        int oxygenLevel = 150; // Invalid

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new VitalSigns(patientId, dateTime, bloodPressure, temperature, pulse, oxygenLevel));
    }
}