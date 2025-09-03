package app.application.usecase;
import app.application.dto.PatientRequest;
import app.application.dto.PatientResponse;
import app.domain.model.Patient;
import app.domain.service.AdministrativeService;

public class RegisterPatientUseCase {
    private final AdministrativeService service;

    public RegisterPatientUseCase(AdministrativeService service) {
        this.service = service;
    }

    public PatientResponse execute(PatientRequest request) {
        Patient patient = service.registerPatient(request.toDomain());
        return PatientResponse.fromDomain(patient);
    }
}