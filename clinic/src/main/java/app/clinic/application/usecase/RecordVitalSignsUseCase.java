package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.VitalSigns;
import app.clinic.domain.service.VitalSignsService;

@Service
public class RecordVitalSignsUseCase {
    private final VitalSignsService vitalSignsService;

    public RecordVitalSignsUseCase(VitalSignsService vitalSignsService) {
        this.vitalSignsService = vitalSignsService;
    }

    public VitalSigns execute(String patientId, String bloodPressure, double temperature, int pulse, int oxygenLevel) {
        return vitalSignsService.recordVitalSigns(patientId, bloodPressure, temperature, pulse, oxygenLevel);
    }
}