package app.domain.services;

import app.domain.exception.DomainValidationException;
import app.domain.model.Patient;
import app.domain.model.VitalSigns;
import app.domain.port.PatientRepository;
import app.domain.port.VitalSignsRepository;
import app.domain.services.AuthenticationService.AuthenticatedUser;

/**
 * Servicio especializado para gestión de signos vitales
 * Sigue principios SOLID:
 * - SRP: Una sola responsabilidad (gestionar signos vitales)
 * - DIP: Depende de abstracciones (repositorios)
 */
public class VitalSignsService {
    private final VitalSignsRepository vitalSignsRepository;
    private final PatientRepository patientRepository;

    public VitalSignsService(VitalSignsRepository vitalSignsRepository, PatientRepository patientRepository) {
        this.vitalSignsRepository = vitalSignsRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Registra signos vitales de un paciente
     */
    public VitalSigns recordVitalSigns(String patientIdCard, int systolic, int diastolic,
                                      int heartRate, double temperature, int oxygenSaturation,
                                      AuthenticatedUser recordedBy) {
        // Validar que el paciente existe
        Patient patient = patientRepository.findByIdCard(patientIdCard)
            .orElseThrow(() -> new DomainValidationException("Patient with ID card " + patientIdCard + " does not exist"));

        // Validar rangos de signos vitales
        validateVitalSigns(systolic, diastolic, heartRate, temperature, oxygenSaturation);

        // Crear signos vitales
        String bloodPressure = systolic + "/" + diastolic;
        VitalSigns vitalSigns = new VitalSigns(
            bloodPressure,
            temperature,
            heartRate,
            oxygenSaturation
        );

        return vitalSignsRepository.save(vitalSigns);
    }

    /**
     * Obtiene los signos vitales más recientes de un paciente
     */
    public VitalSigns getLatestVitalSigns(String patientIdCard) {
        if (!patientRepository.existsByIdCard(patientIdCard)) {
            throw new DomainValidationException("Patient with ID card " + patientIdCard + " does not exist");
        }

        return vitalSignsRepository.findLatestByPatientIdCard(patientIdCard)
            .orElseThrow(() -> new DomainValidationException("No vital signs found for patient " + patientIdCard));
    }

    /**
     * Valida que un usuario pueda registrar signos vitales
     */
    public void validateCanRecordVitalSigns(AuthenticatedUser user) {
        if (!user.canRecordVitalSigns()) {
            throw new DomainValidationException("User " + user.getIdCard() + " is not authorized to record vital signs");
        }
    }

    /**
     * Valida rangos de signos vitales
     */
    private void validateVitalSigns(int systolic, int diastolic, int heartRate, double temperature, int oxygenSaturation) {
        if (systolic < 50 || systolic > 300) {
            throw new DomainValidationException("Systolic pressure must be between 50 and 300 mmHg");
        }

        if (diastolic < 30 || diastolic > 200) {
            throw new DomainValidationException("Diastolic pressure must be between 30 and 200 mmHg");
        }

        if (heartRate < 30 || heartRate > 250) {
            throw new DomainValidationException("Heart rate must be between 30 and 250 bpm");
        }

        if (temperature < 30.0 || temperature > 45.0) {
            throw new DomainValidationException("Temperature must be between 30.0 and 45.0 °C");
        }

        if (oxygenSaturation < 50 || oxygenSaturation > 100) {
            throw new DomainValidationException("Oxygen saturation must be between 50 and 100%");
        }
    }
}