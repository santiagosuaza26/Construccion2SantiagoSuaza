package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.VitalSignsService;

@Service
public class CompleteHospitalizationUseCase {
    private final VitalSignsService vitalSignsService;

    public CompleteHospitalizationUseCase(VitalSignsService vitalSignsService) {
        this.vitalSignsService = vitalSignsService;
    }

    public void execute(String patientId, String nurseId, String hospitalizationDetails) {
        // Record completion of hospitalization
        vitalSignsService.recordVitalSignsWithObservations(patientId, "N/A", 36.5, 70, 98, "Hospitalization completed: " + hospitalizationDetails);
    }
}