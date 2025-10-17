package app.clinic.application.usecase;

import org.springframework.stereotype.Service;

import app.clinic.application.dto.patient.CreatePatientDTO;
import app.clinic.application.dto.patient.PatientDTO;
import app.clinic.application.mapper.PatientMapper;
import app.clinic.domain.model.Patient;
import app.clinic.domain.service.PatientDomainService;

/**
 * Caso de uso para registrar un nuevo paciente.
 * Coordina entre la capa de presentación y los servicios del dominio.
 */
@Service
public class RegisterPatientUseCase {

    private final PatientDomainService patientDomainService;

    public RegisterPatientUseCase(PatientDomainService patientDomainService) {
        this.patientDomainService = patientDomainService;
    }

    /**
     * Ejecuta el caso de uso de registro de paciente.
     *
     * @param request DTO con información del paciente a registrar
     * @return DTO con información del paciente registrado
     * @throws IllegalArgumentException si los datos del paciente son inválidos
     */
    public PatientDTO execute(CreatePatientDTO request) {
        // Validar datos de entrada
        validateRequest(request);

        // Mapear DTO a entidad del dominio
        Patient patient = PatientMapper.toDomainEntity(request);

        // Ejecutar lógica del dominio (independiente)
        Patient registeredPatient = patientDomainService.registerPatient(patient);

        // Mapear entidad del dominio a DTO de respuesta
        return PatientMapper.toDTO(registeredPatient);
    }

    /**
     * Valida los datos de la solicitud de registro.
     */
    private void validateRequest(CreatePatientDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Patient registration request cannot be null");
        }

        if (request.getCedula() == null || request.getCedula().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient cedula is required");
        }

        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient full name is required");
        }

        if (request.getBirthDate() == null) {
            throw new IllegalArgumentException("Patient birth date is required");
        }
    }
}