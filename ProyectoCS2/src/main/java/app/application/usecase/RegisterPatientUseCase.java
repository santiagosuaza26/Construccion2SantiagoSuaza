package app.application.usecase;
import com.clinic.application.dto.PatientRequest;
import com.clinic.application.dto.PatientResponse;
import com.clinic.domain.model.Patient;
import com.clinic.domain.service.AdministrativeService;

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