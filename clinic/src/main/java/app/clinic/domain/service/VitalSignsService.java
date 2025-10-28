package app.clinic.domain.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.User;
import app.clinic.domain.model.entities.VitalSigns;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;

@Service
public class VitalSignsService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final RoleBasedAccessService roleBasedAccessService;

    public VitalSignsService(PatientRepository patientRepository, UserRepository userRepository, RoleBasedAccessService roleBasedAccessService) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public VitalSigns recordVitalSigns(String patientId, String bloodPressure, double temperature, int pulse, int oxygenLevel) {
        return recordVitalSignsWithObservations(patientId, bloodPressure, temperature, pulse, oxygenLevel, "");
    }

    public VitalSigns recordVitalSignsWithObservations(String patientId, String bloodPressure, double temperature, int pulse, int oxygenLevel, String observations) {
        Id id = new Id(patientId);
        if (!patientRepository.existsByIdentificationNumber(id)) {
            throw new IllegalArgumentException("Patient not found");
        }
        VitalSigns vitalSigns = new VitalSigns(patientId, LocalDateTime.now(), bloodPressure, temperature, pulse, oxygenLevel, observations);
        patientRepository.saveVitalSigns(vitalSigns);
        return vitalSigns;
    }

    public void recordMedicationAdministration(String patientId, String nurseId, String orderNumber, int item, String administrationDetails) {
        validateNurseRole(nurseId);
        Id patientIdObj = new Id(patientId);
        if (!patientRepository.existsByIdentificationNumber(patientIdObj)) {
            throw new IllegalArgumentException("Patient not found");
        }
        // Validate that the order exists and has the medication item
        // For simplicity, assume orderRepository has a method to find by orderNumber and check item
        // In real implementation, add method to OrderRepository
        // For now, just validate and assume it's saved
        // patientRepository.saveMedicationAdministration(patientId, nurseId, orderNumber, item, administrationDetails, LocalDateTime.now());
    }

    public void recordProcedureRealization(String patientId, String nurseId, String orderNumber, int item, String realizationDetails) {
        validateNurseRole(nurseId);
        Id patientIdObj = new Id(patientId);
        if (!patientRepository.existsByIdentificationNumber(patientIdObj)) {
            throw new IllegalArgumentException("Patient not found");
        }
        // Validate that the order exists and has the procedure item
        // For simplicity, assume orderRepository has a method to find by orderNumber and check item
        // In real implementation, add method to OrderRepository
        // For now, just validate and assume it's saved
        // patientRepository.saveProcedureRealization(patientId, nurseId, orderNumber, item, realizationDetails, LocalDateTime.now());
    }

    private void validateNurseRole(String nurseId) {
        Id nurseIdObj = new Id(nurseId);
        User nurse = userRepository.findByIdentificationNumber(nurseIdObj).orElseThrow(() -> new IllegalArgumentException("Nurse not found"));
        roleBasedAccessService.checkAccess(nurse.getRole(), "medicationAdministration");
    }
}