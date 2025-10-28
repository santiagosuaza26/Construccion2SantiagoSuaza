package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.VitalSignsService;

@Service
public class RecordMedicationAdministrationUseCase {
    private final VitalSignsService vitalSignsService;

    public RecordMedicationAdministrationUseCase(VitalSignsService vitalSignsService) {
        this.vitalSignsService = vitalSignsService;
    }

    public void execute(String patientId, String nurseId, String orderNumber, int item, String administrationDetails) {
        vitalSignsService.recordMedicationAdministration(patientId, nurseId, orderNumber, item, administrationDetails);
    }
}