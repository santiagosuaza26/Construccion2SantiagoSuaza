package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.service.PatientService;

@Service
public class GetPatientUseCase {
    private final PatientService patientService;

    public GetPatientUseCase(PatientService patientService) {
        this.patientService = patientService;
    }

    public Patient execute(String identificationNumber) {
        return patientService.findPatientById(identificationNumber);
    }
}