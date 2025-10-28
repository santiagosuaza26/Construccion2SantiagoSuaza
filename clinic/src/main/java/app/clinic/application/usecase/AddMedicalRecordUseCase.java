package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.MedicalRecordService;

@Service
public class AddMedicalRecordUseCase {
    private final MedicalRecordService medicalRecordService;

    public AddMedicalRecordUseCase(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    public void execute(String patientId, String doctorId, String reason, String symptoms, String diagnosis) {
        medicalRecordService.addRecord(patientId, doctorId, reason, symptoms, diagnosis);
    }
}