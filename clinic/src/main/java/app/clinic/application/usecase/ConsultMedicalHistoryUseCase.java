package app.clinic.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.MedicalRecord;
import app.clinic.domain.service.MedicalRecordService;

@Service
public class ConsultMedicalHistoryUseCase {
    private final MedicalRecordService medicalRecordService;

    public ConsultMedicalHistoryUseCase(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    public List<MedicalRecord> execute(String patientId) {
        return medicalRecordService.getMedicalHistory(patientId);
    }
}