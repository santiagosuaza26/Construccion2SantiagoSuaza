package app.clinic.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.service.PatientService;

@Service
public class ListPatientsUseCase {
    private final PatientService patientService;

    public ListPatientsUseCase(PatientService patientService) {
        this.patientService = patientService;
    }

    public List<Patient> execute() {
        return patientService.getAllPatients();
    }
}