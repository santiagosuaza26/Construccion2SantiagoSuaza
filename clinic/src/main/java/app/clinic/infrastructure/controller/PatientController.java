package app.clinic.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.RegisterPatientUseCase;
import app.clinic.application.usecase.UpdatePatientUseCase;
import app.clinic.infrastructure.dto.PatientDTO;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final RegisterPatientUseCase registerPatientUseCase;
    private final UpdatePatientUseCase updatePatientUseCase;

    public PatientController(RegisterPatientUseCase registerPatientUseCase,
                            UpdatePatientUseCase updatePatientUseCase) {
        this.registerPatientUseCase = registerPatientUseCase;
        this.updatePatientUseCase = updatePatientUseCase;
    }

    @PostMapping
    public ResponseEntity<PatientDTO> registerPatient(@RequestBody RegisterPatientRequest request) {
        var patient = registerPatientUseCase.execute(
            request.identificationNumber, request.fullName, request.dateOfBirth, request.gender,
            request.address, request.phone, request.email, request.password, request.emergencyName, request.emergencyRelation,
            request.emergencyPhone, request.companyName, request.policyNumber, request.insuranceActive,
            request.validityDate
        );

        var dto = new PatientDTO(
            patient.getIdentificationNumber().getValue(),
            patient.getFullName(),
            patient.getDateOfBirth().toString(),
            patient.getGender().toString(),
            patient.getAddress().getValue(),
            patient.getPhone().getValue(),
            patient.getEmail().getValue(),
            patient.getEmergencyContact().getName(),
            patient.getEmergencyContact().getRelation(),
            patient.getEmergencyContact().getPhone().getValue(),
            patient.getInsurance().getCompanyName(),
            patient.getInsurance().getPolicyNumber(),
            patient.getInsurance().isActive(),
            patient.getInsurance().getValidityDate().toString()
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable String id, @RequestBody UpdatePatientRequest request) {
        var patient = updatePatientUseCase.execute(
            id, request.fullName, request.dateOfBirth, request.gender, request.address, request.phone,
            request.email, request.emergencyName, request.emergencyRelation, request.emergencyPhone,
            request.companyName, request.policyNumber, request.insuranceActive, request.validityDate
        );

        var dto = new PatientDTO(
            patient.getIdentificationNumber().getValue(),
            patient.getFullName(),
            patient.getDateOfBirth().toString(),
            patient.getGender().toString(),
            patient.getAddress().getValue(),
            patient.getPhone().getValue(),
            patient.getEmail().getValue(),
            patient.getEmergencyContact().getName(),
            patient.getEmergencyContact().getRelation(),
            patient.getEmergencyContact().getPhone().getValue(),
            patient.getInsurance().getCompanyName(),
            patient.getInsurance().getPolicyNumber(),
            patient.getInsurance().isActive(),
            patient.getInsurance().getValidityDate().toString()
        );

        return ResponseEntity.ok(dto);
    }

    public static class RegisterPatientRequest {
        public String identificationNumber;
        public String fullName;
        public String dateOfBirth;
        public String gender;
        public String address;
        public String phone;
        public String email;
        public String password;
        public String emergencyName;
        public String emergencyRelation;
        public String emergencyPhone;
        public String companyName;
        public String policyNumber;
        public boolean insuranceActive;
        public String validityDate;
    }

    public static class UpdatePatientRequest {
        public String fullName;
        public String dateOfBirth;
        public String gender;
        public String address;
        public String phone;
        public String email;
        public String emergencyName;
        public String emergencyRelation;
        public String emergencyPhone;
        public String companyName;
        public String policyNumber;
        public boolean insuranceActive;
        public String validityDate;
    }
}