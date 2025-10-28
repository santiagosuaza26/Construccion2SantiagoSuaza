package app.clinic.application.usecase;

import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.service.PatientService;

public class RegisterEmergencyContactUseCase {
    private final PatientService patientService;

    public RegisterEmergencyContactUseCase(PatientService patientService) {
        this.patientService = patientService;
    }

    public Patient execute(String patientId, String emergencyName, String emergencyRelation, String emergencyPhone) {
        Patient patient = patientService.findPatientById(patientId);
        // Update emergency contact - this would require a new method in PatientService
        // For now, we'll use the existing updatePatient method with current patient data
        patientService.updatePatient(
            patientId,
            patient.getFullName(),
            patient.getDateOfBirth().toString(),
            patient.getGender().toString(),
            patient.getAddress().getValue(),
            patient.getPhone().getValue(),
            patient.getEmail().getValue(),
            emergencyName,
            emergencyRelation,
            emergencyPhone,
            patient.getInsurance().getCompanyName(),
            patient.getInsurance().getPolicyNumber(),
            patient.getInsurance().isActive(),
            patient.getInsurance().getValidityDate().toString()
        );
        return patientService.findPatientById(patientId);
    }
}