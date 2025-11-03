package app.clinic.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.VitalSigns;
import app.clinic.domain.service.PatientService;

@Service
public class ConsultPatientVitalSignsUseCase {
    private final PatientService patientService;

    public ConsultPatientVitalSignsUseCase(PatientService patientService) {
        this.patientService = patientService;
    }

    public List<VitalSigns> execute(String patientId) {
        return patientService.findVitalSignsByPatientId(patientId);
    }
}