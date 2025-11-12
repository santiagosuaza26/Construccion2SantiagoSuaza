package app.clinic.infrastructure.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.VitalSigns;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;
import app.clinic.domain.service.RoleBasedAccessService;
import app.clinic.domain.service.VitalSignsService;

@Service
public class NursingServiceImpl {

    private final VitalSignsService vitalSignsService;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final RoleBasedAccessService roleBasedAccessService;

    public NursingServiceImpl(VitalSignsService vitalSignsService,
                             PatientRepository patientRepository,
                             UserRepository userRepository,
                             RoleBasedAccessService roleBasedAccessService) {
        this.vitalSignsService = vitalSignsService;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public void registerPatientVisit(String patientId, String nurseId, LocalDate visitDate) {
        // Validate nurse role
        var nurse = userRepository.findByIdentificationNumber(new app.clinic.domain.model.valueobject.Id(nurseId))
                .orElseThrow(() -> new RuntimeException("Enfermera no encontrada"));
        roleBasedAccessService.checkAccess(nurse.getRole(), "PATIENT_DATA");

        // Validate patient exists
        patientRepository.findByIdentificationNumber(new app.clinic.domain.model.valueobject.Id(patientId))
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Register visit (could be stored in a separate table or just logged)
        // For now, we'll assume visits are tracked via vital signs or separate entity
    }

    public VitalSigns recordVitalSigns(String patientId, String nurseId, String bloodPressure, double temperature, int pulse, int oxygenLevel) {
        return vitalSignsService.recordVitalSigns(patientId, bloodPressure, temperature, pulse, oxygenLevel);
    }

    public VitalSigns recordVitalSignsWithObservations(String patientId, String nurseId, String bloodPressure, double temperature, int pulse, int oxygenLevel, String observations) {
        return vitalSignsService.recordVitalSignsWithObservations(patientId, bloodPressure, temperature, pulse, oxygenLevel, observations);
    }

    public void recordMedicationAdministration(String patientId, String nurseId, String orderNumber, int item, String administrationDetails) {
        vitalSignsService.recordMedicationAdministration(patientId, nurseId, orderNumber, item, administrationDetails);
    }

    public void recordProcedureRealization(String patientId, String nurseId, String orderNumber, int item, String realizationDetails) {
        vitalSignsService.recordProcedureRealization(patientId, nurseId, orderNumber, item, realizationDetails);
    }

    public void recordDiagnosticAidResult(String patientId, String nurseId, String orderNumber, int item, String resultDetails) {
        vitalSignsService.recordDiagnosticAidResult(patientId, nurseId, orderNumber, item, resultDetails);
    }

    public void addNurseObservations(String patientId, String nurseId, String observation) {
        // Validate nurse role
        var nurse = userRepository.findByIdentificationNumber(new app.clinic.domain.model.valueobject.Id(nurseId))
                .orElseThrow(() -> new RuntimeException("Enfermera no encontrada"));
        roleBasedAccessService.checkAccess(nurse.getRole(), "PATIENT_DATA");

        // Validate patient exists
        patientRepository.findByIdentificationNumber(new app.clinic.domain.model.valueobject.Id(patientId))
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Add observation to vital signs or medical record
        // For simplicity, we'll record it as vital signs with observations
        vitalSignsService.recordVitalSignsWithObservations(patientId, "N/A", 0.0, 0, 0, observation);
    }

    public List<VitalSigns> getPatientVitalSigns(String patientId) {
        return patientRepository.findVitalSignsByPatientId(new app.clinic.domain.model.valueobject.Id(patientId));
    }
}