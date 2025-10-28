package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.VitalSignsService;

@Service
public class RecordProcedureRealizationUseCase {
    private final VitalSignsService vitalSignsService;

    public RecordProcedureRealizationUseCase(VitalSignsService vitalSignsService) {
        this.vitalSignsService = vitalSignsService;
    }

    public void execute(String patientId, String nurseId, String orderNumber, int item, String realizationDetails) {
        vitalSignsService.recordProcedureRealization(patientId, nurseId, orderNumber, item, realizationDetails);
    }
}