package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.service.PatientService;

@Service
public class ValidatePatientDataUseCase {
    private final PatientService patientService;

    public ValidatePatientDataUseCase(PatientService patientService) {
        this.patientService = patientService;
    }

    public void execute(String identificationNumber, String fullName, String dateOfBirth, String gender,
                       String address, String phone, String email, String emergencyName, String emergencyRelation,
                       String emergencyPhone, String companyName, String policyNumber, boolean insuranceActive,
                       String validityDate) {
        patientService.validatePatientData(identificationNumber, fullName, dateOfBirth, gender, address,
                                         phone, email, emergencyName, emergencyRelation, emergencyPhone,
                                         companyName, policyNumber, insuranceActive, validityDate);
    }
}