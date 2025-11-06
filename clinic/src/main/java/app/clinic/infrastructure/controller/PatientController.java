package app.clinic.infrastructure.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.ConsultPatientOrdersUseCase;
import app.clinic.application.usecase.ListPatientsUseCase;
import app.clinic.application.usecase.RegisterPatientUseCase;
import app.clinic.application.usecase.UpdatePatientUseCase;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.repository.AppointmentRepository;
import app.clinic.domain.service.PatientService;
import app.clinic.domain.service.UserService;
import app.clinic.infrastructure.dto.AppointmentDTO;
import app.clinic.infrastructure.dto.OrderDTO;
import app.clinic.infrastructure.dto.PatientDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final RegisterPatientUseCase registerPatientUseCase;
    private final UpdatePatientUseCase updatePatientUseCase;
    private final ListPatientsUseCase listPatientsUseCase;
    private final ConsultPatientOrdersUseCase consultPatientOrdersUseCase;
    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;
    private final UserService userService;

    public PatientController(RegisterPatientUseCase registerPatientUseCase,
                             UpdatePatientUseCase updatePatientUseCase,
                             ListPatientsUseCase listPatientsUseCase,
                             ConsultPatientOrdersUseCase consultPatientOrdersUseCase,
                             AppointmentRepository appointmentRepository,
                             PatientService patientService,
                             UserService userService) {
        this.registerPatientUseCase = registerPatientUseCase;
        this.updatePatientUseCase = updatePatientUseCase;
        this.listPatientsUseCase = listPatientsUseCase;
        this.consultPatientOrdersUseCase = consultPatientOrdersUseCase;
        this.appointmentRepository = appointmentRepository;
        this.patientService = patientService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('PERSONAL_ADMINISTRATIVO')")
    public ResponseEntity<PatientDTO> registerPatient(@Valid @RequestBody RegisterPatientRequest request) {
        var patient = registerPatientUseCase.execute(
            request.identificationNumber, request.fullName, request.dateOfBirth, request.gender,
            request.address, request.phone, request.email, request.emergencyName, request.emergencyRelation,
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
    @PreAuthorize("hasRole('PERSONAL_ADMINISTRATIVO')")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable String id, @Valid @RequestBody UpdatePatientRequest request) {
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

    @GetMapping
    @PreAuthorize("hasAnyRole('PERSONAL_ADMINISTRATIVO', 'MEDICO', 'ENFERMERA')")
    public ResponseEntity<List<PatientDTO>> listPatients() {
        var patients = listPatientsUseCase.execute();

        var patientDTOs = patients.stream().map(patient -> new PatientDTO(
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
        )).collect(Collectors.toList());

        return ResponseEntity.ok(patientDTOs);
    }

    @GetMapping("/{patientId}/appointments")
    @PreAuthorize("hasAnyRole('PERSONAL_ADMINISTRATIVO', 'MEDICO')")
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

    @GetMapping("/{patientId}/orders")
    @PreAuthorize("hasAnyRole('PERSONAL_ADMINISTRATIVO', 'MEDICO', 'ENFERMERA')")
    public ResponseEntity<List<OrderDTO>> getPatientOrders(@PathVariable String patientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roleString = authentication.getAuthorities().iterator().next().getAuthority();
        if (roleString.startsWith("ROLE_")) {
            roleString = roleString.substring(5);
        }
        Role userRole = Role.valueOf(roleString);

        var orders = consultPatientOrdersUseCase.execute(userRole, patientId);

        var orderDTOs = orders.stream().map(order -> new OrderDTO(
            order.getOrderNumber().getValue(),
            order.getPatientIdentificationNumber(),
            order.getDoctorIdentificationNumber(),
            order.getDate(),
            order.getDiagnosis(),
            order.getMedications().stream().map(m -> String.valueOf(m.getItem())).collect(Collectors.toList()),
            order.getProcedures().stream().map(p -> String.valueOf(p.getItem())).collect(Collectors.toList()),
            order.getDiagnosticAids().stream().map(d -> String.valueOf(d.getItem())).collect(Collectors.toList())
        )).collect(Collectors.toList());

        return ResponseEntity.ok(orderDTOs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PERSONAL_ADMINISTRATIVO', 'MEDICO', 'ENFERMERA')")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable String id) {
        var patient = patientService.findPatientById(id);

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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PERSONAL_ADMINISTRATIVO')")
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/emergency-contact")
    @PreAuthorize("hasRole('PERSONAL_ADMINISTRATIVO')")
    public ResponseEntity<Void> addEmergencyContact(@PathVariable String id, @RequestBody EmergencyContactRequest request) {
        // Note: Emergency contact is managed during patient registration/update
        // This endpoint could be used to update emergency contact separately
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/emergency-contact")
    @PreAuthorize("hasRole('PERSONAL_ADMINISTRATIVO')")
    public ResponseEntity<Void> updateEmergencyContact(@PathVariable String id, @RequestBody EmergencyContactRequest request) {
        // Update emergency contact logic would go here
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/emergency-contact")
    @PreAuthorize("hasRole('PERSONAL_ADMINISTRATIVO')")
    public ResponseEntity<Void> deleteEmergencyContact(@PathVariable String id) {
        // Delete emergency contact logic would go here
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/insurance")
    @PreAuthorize("hasRole('PERSONAL_ADMINISTRATIVO')")
    public ResponseEntity<Void> addInsurance(@PathVariable String id, @RequestBody InsuranceRequest request) {
        // Add insurance logic would go here
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/insurance")
    @PreAuthorize("hasRole('PERSONAL_ADMINISTRATIVO')")
    public ResponseEntity<Void> updateInsurance(@PathVariable String id, @RequestBody InsuranceRequest request) {
        // Update insurance logic would go here
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/insurance")
    @PreAuthorize("hasRole('PERSONAL_ADMINISTRATIVO')")
    public ResponseEntity<Void> deleteInsurance(@PathVariable String id) {
        // Delete insurance logic would go here
        return ResponseEntity.noContent().build();
    }

    public static class RegisterPatientRequest {
        @jakarta.validation.constraints.NotBlank(message = "El número de identificación es obligatorio")
        @jakarta.validation.constraints.Pattern(regexp = "\\d{1,10}", message = "El número de identificación debe contener entre 1 y 10 dígitos")
        public String identificationNumber;

        @jakarta.validation.constraints.NotBlank(message = "El nombre completo es obligatorio")
        public String fullName;

        @jakarta.validation.constraints.NotBlank(message = "La fecha de nacimiento es obligatoria")
        @jakarta.validation.constraints.Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "La fecha debe tener formato DD/MM/YYYY")
        public String dateOfBirth;

        @jakarta.validation.constraints.NotBlank(message = "El género es obligatorio")
        public String gender;

        @jakarta.validation.constraints.NotBlank(message = "La dirección es obligatoria")
        @jakarta.validation.constraints.Size(max = 30, message = "La dirección no puede exceder 30 caracteres")
        public String address;

        @jakarta.validation.constraints.NotBlank(message = "El teléfono es obligatorio")
        @jakarta.validation.constraints.Pattern(regexp = "\\d{10}", message = "El teléfono debe contener exactamente 10 dígitos")
        public String phone;

        @jakarta.validation.constraints.NotBlank(message = "El email es obligatorio")
        @jakarta.validation.constraints.Email(message = "El email debe tener un formato válido")
        public String email;

        @jakarta.validation.constraints.NotBlank(message = "El nombre del contacto de emergencia es obligatorio")
        public String emergencyName;

        @jakarta.validation.constraints.NotBlank(message = "La relación del contacto de emergencia es obligatoria")
        public String emergencyRelation;

        @jakarta.validation.constraints.NotBlank(message = "El teléfono del contacto de emergencia es obligatorio")
        @jakarta.validation.constraints.Pattern(regexp = "\\d{10}", message = "El teléfono de emergencia debe contener exactamente 10 dígitos")
        public String emergencyPhone;

        public String companyName;
        public String policyNumber;
        public boolean insuranceActive;
        public String validityDate;
    }

    public static class UpdatePatientRequest {
        @jakarta.validation.constraints.NotBlank(message = "El nombre completo es obligatorio")
        public String fullName;

        @jakarta.validation.constraints.NotBlank(message = "La fecha de nacimiento es obligatoria")
        @jakarta.validation.constraints.Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "La fecha debe tener formato DD/MM/YYYY")
        public String dateOfBirth;

        @jakarta.validation.constraints.NotBlank(message = "El género es obligatorio")
        public String gender;

        @jakarta.validation.constraints.NotBlank(message = "La dirección es obligatoria")
        @jakarta.validation.constraints.Size(max = 30, message = "La dirección no puede exceder 30 caracteres")
        public String address;

        @jakarta.validation.constraints.NotBlank(message = "El teléfono es obligatorio")
        @jakarta.validation.constraints.Pattern(regexp = "\\d{10}", message = "El teléfono debe contener exactamente 10 dígitos")
        public String phone;

        @jakarta.validation.constraints.NotBlank(message = "El email es obligatorio")
        @jakarta.validation.constraints.Email(message = "El email debe tener un formato válido")
        public String email;

        @jakarta.validation.constraints.NotBlank(message = "El nombre del contacto de emergencia es obligatorio")
        public String emergencyName;

        @jakarta.validation.constraints.NotBlank(message = "La relación del contacto de emergencia es obligatoria")
        public String emergencyRelation;

        @jakarta.validation.constraints.NotBlank(message = "El teléfono del contacto de emergencia es obligatorio")
        @jakarta.validation.constraints.Pattern(regexp = "\\d{10}", message = "El teléfono de emergencia debe contener exactamente 10 dígitos")
        public String emergencyPhone;

        public String companyName;
        public String policyNumber;
        public boolean insuranceActive;
        public String validityDate;
    }

    public static class EmergencyContactRequest {
        @jakarta.validation.constraints.NotBlank(message = "El nombre del contacto de emergencia es obligatorio")
        public String name;

        @jakarta.validation.constraints.NotBlank(message = "La relación del contacto de emergencia es obligatoria")
        public String relation;

        @jakarta.validation.constraints.NotBlank(message = "El teléfono del contacto de emergencia es obligatorio")
        @jakarta.validation.constraints.Pattern(regexp = "\\d{10}", message = "El teléfono de emergencia debe contener exactamente 10 dígitos")
        public String phone;
    }

    public static class InsuranceRequest {
        @jakarta.validation.constraints.NotBlank(message = "El nombre de la compañía de seguros es obligatorio")
        public String companyName;

        @jakarta.validation.constraints.NotBlank(message = "El número de póliza es obligatorio")
        public String policyNumber;

        public boolean active;

        @jakarta.validation.constraints.NotBlank(message = "La fecha de validez es obligatoria")
        @jakarta.validation.constraints.Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "La fecha debe tener formato DD/MM/YYYY")
        public String validityDate;
    }
}