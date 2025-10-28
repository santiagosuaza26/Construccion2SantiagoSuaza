package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.MedicalRecord;
import app.clinic.domain.service.MedicalRecordService;

@Service
public class GetMedicalRecordUseCase {
    private final MedicalRecordService medicalRecordService;

    public GetMedicalRecordUseCase(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    public MedicalRecord execute(String patientId) {
        return medicalRecordService.getOrCreateMedicalRecord(patientId);
    }
}