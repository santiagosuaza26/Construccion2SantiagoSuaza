package app.clinic.infrastructure.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.RegisterPatientUseCase;
import app.clinic.application.usecase.UpdatePatientUseCase;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.repository.AppointmentRepository;
import app.clinic.domain.service.PatientService;
import app.clinic.domain.service.UserService;
import app.clinic.infrastructure.dto.AppointmentDTO;
import app.clinic.infrastructure.dto.PatientDTO;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final RegisterPatientUseCase registerPatientUseCase;
    private final UpdatePatientUseCase updatePatientUseCase;
    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;
    private final UserService userService;

    public PatientController(RegisterPatientUseCase registerPatientUseCase,
                            UpdatePatientUseCase updatePatientUseCase,
                            AppointmentRepository appointmentRepository,
                            PatientService patientService,
                            UserService userService) {
        this.registerPatientUseCase = registerPatientUseCase;
        this.updatePatientUseCase = updatePatientUseCase;
        this.appointmentRepository = appointmentRepository;
        this.patientService = patientService;
        this.userService = userService;
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

    @GetMapping("/{patientId}/appointments")
    public ResponseEntity<List<AppointmentDTO>> getPatientAppointments(@PathVariable String patientId) {
        var appointments = appointmentRepository.findByPatientId(new Id(patientId));
        var patient = patientService.findPatientById(patientId);

        var appointmentDTOs = appointments.stream().map(appointment -> {
            var doctor = userService.findUserById(appointment.getDoctorId().getValue());
            return new AppointmentDTO(
                appointment.getPatientId().getValue(), // id
                appointment.getPatientId().getValue(), // patientId
                patient.getFullName(), // patientName
                null, // adminId - not available in domain model
                null, // adminName - not available
                appointment.getDoctorId().getValue(), // doctorId
                doctor.getFullName(), // doctorName
                appointment.getDateTime(), // dateTime
                appointment.getReason(), // reason
                "SCHEDULED" // status - default since not in domain model
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(appointmentDTOs);
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