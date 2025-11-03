package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.VitalSignsService;

@Service
public class RecordDiagnosticAidResultUseCase {
    private final VitalSignsService vitalSignsService;

    public RecordDiagnosticAidResultUseCase(VitalSignsService vitalSignsService) {
        this.vitalSignsService = vitalSignsService;
    }

    public void execute(String patientId, String nurseId, String orderNumber, int item, String resultDetails) {
        vitalSignsService.recordDiagnosticAidResult(patientId, nurseId, orderNumber, item, resultDetails);
    }
}