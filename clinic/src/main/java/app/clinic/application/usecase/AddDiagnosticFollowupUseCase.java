package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.MedicalRecordService;

@Service
public class AddDiagnosticFollowupUseCase {
    private final MedicalRecordService medicalRecordService;

    public AddDiagnosticFollowupUseCase(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    public void execute(String patientId, String diagnosis, String orderNumber, String medicationId, String dosage, String duration) {
        // Add diagnosis to medical record
        medicalRecordService.addRecord(patientId, "doctor-id", "Diagnostic Follow-up", "Follow-up after diagnostic aid", diagnosis);

        // Add medication to record if prescribed
        if (medicationId != null && !medicationId.isEmpty()) {
            medicalRecordService.addMedicationToRecord(patientId, orderNumber, medicationId, dosage, duration);
        }
    }
}