package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.MedicalRecordService;

@Service
public class UpdateMedicalRecordUseCase {
    private final MedicalRecordService medicalRecordService;

    public UpdateMedicalRecordUseCase(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    public void execute(String patientId, String doctorId, String reason, String symptoms, String diagnosis) {
        // For update, we add a new record (history is maintained)
        medicalRecordService.addRecord(patientId, doctorId, reason, symptoms, diagnosis);
    }
}