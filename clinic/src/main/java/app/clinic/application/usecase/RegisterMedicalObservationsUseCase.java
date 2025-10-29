package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.VitalSignsService;

@Service
public class RegisterMedicalObservationsUseCase {
    private final VitalSignsService vitalSignsService;

    public RegisterMedicalObservationsUseCase(VitalSignsService vitalSignsService) {
        this.vitalSignsService = vitalSignsService;
    }

    public void execute(String patientId, String nurseId, String observations) {
        // Register observations as part of vital signs
        // For now, we'll record with default vital signs values and add observations
        // In a real implementation, this might be a separate entity or method
        vitalSignsService.recordVitalSignsWithObservations(patientId, "N/A", 36.5, 70, 98, observations);
    }
}