package app.clinic.domain.service;

import app.clinic.domain.model.entities.Insurance;
import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.model.valueobject.Address;
import app.clinic.domain.model.valueobject.DateOfBirth;
import app.clinic.domain.model.valueobject.Email;
import app.clinic.domain.model.valueobject.EmergencyContact;
import app.clinic.domain.model.valueobject.Gender;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Phone;
import app.clinic.domain.repository.PatientRepository;

public class PatientService {
    private final PatientRepository patientRepository;
    private final RoleBasedAccessService roleBasedAccessService;

    public PatientService(PatientRepository patientRepository, RoleBasedAccessService roleBasedAccessService) {
        this.patientRepository = patientRepository;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public Patient registerPatient(String identificationNumber, String fullName, String dateOfBirth, String gender, String address, String phone, String email, String emergencyName, String emergencyRelation, String emergencyPhone, String companyName, String policyNumber, boolean insuranceActive, String validityDate) {
        // Validar todos los datos del paciente
        validatePatientData(identificationNumber, fullName, dateOfBirth, gender, address, phone, email, emergencyName, emergencyRelation, emergencyPhone, companyName, policyNumber, insuranceActive, validityDate);

        EmergencyContact emergencyContact = new EmergencyContact(emergencyName, emergencyRelation, new Phone(emergencyPhone));
        Insurance insurance = new Insurance(companyName, policyNumber, insuranceActive, java.time.LocalDate.parse(validityDate, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        Patient patient = new Patient(new Id(identificationNumber), fullName, new DateOfBirth(dateOfBirth), Gender.valueOf(gender.toUpperCase()), new Address(address), new Phone(phone), new Email(email), emergencyContact, insurance);
        patientRepository.save(patient);
        return patient;
    }

    public void updatePatient(String identificationNumber, String fullName, String dateOfBirth, String gender, String address, String phone, String email, String emergencyName, String emergencyRelation, String emergencyPhone, String companyName, String policyNumber, boolean insuranceActive, String validityDate) {
        Id id = new Id(identificationNumber);
        patientRepository.findByIdentificationNumber(id).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        EmergencyContact emergencyContact = new EmergencyContact(emergencyName, emergencyRelation, new Phone(emergencyPhone));
        Insurance insurance = new Insurance(companyName, policyNumber, insuranceActive, java.time.LocalDate.parse(validityDate));
        Patient updatedPatient = new Patient(id, fullName, new DateOfBirth(dateOfBirth), Gender.valueOf(gender.toUpperCase()), new Address(address), new Phone(phone), new Email(email), emergencyContact, insurance);
        patientRepository.save(updatedPatient);
    }

    public Patient findPatientById(String identificationNumber, app.clinic.domain.model.valueobject.Role currentUserRole) {
        // Validar que el usuario tenga permisos para acceder a datos de pacientes
        roleBasedAccessService.validatePatientDataAccess(currentUserRole, false);

        Id id = new Id(identificationNumber);
        return patientRepository.findByIdentificationNumber(id).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    // Método sobrecargado para compatibilidad con código existente (debe ser usado con precaución)
    @Deprecated
    public Patient findPatientById(String identificationNumber) {
        // Nota: Este método no valida permisos. Debe ser usado solo en contextos donde ya se validaron permisos
        Id id = new Id(identificationNumber);
        return patientRepository.findByIdentificationNumber(id).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    public void deletePatient(String identificationNumber) {
        Id id = new Id(identificationNumber);
        if (!patientRepository.existsByIdentificationNumber(id)) {
            throw new IllegalArgumentException("Patient not found");
        }
        patientRepository.deleteByIdentificationNumber(id);
    }

    public java.util.List<app.clinic.domain.model.entities.VitalSigns> findVitalSignsByPatientId(String patientId) {
        Id id = new Id(patientId);
        return patientRepository.findVitalSignsByPatientId(id);
    }

    public void validatePatientData(String identificationNumber, String fullName, String dateOfBirth, String gender,
                                   String address, String phone, String email, String emergencyName, String emergencyRelation,
                                   String emergencyPhone, String companyName, String policyNumber, boolean insuranceActive,
                                   String validityDate) {
        // Validar cédula única
        if (identificationNumber == null || identificationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Identification number is required");
        }
        if (patientRepository.existsByIdentificationNumber(new Id(identificationNumber))) {
            throw new IllegalArgumentException("Identification number already exists: " + identificationNumber);
        }

        // Validar nombre completo
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }

        // Validar fecha de nacimiento
        if (dateOfBirth == null || dateOfBirth.trim().isEmpty()) {
            throw new IllegalArgumentException("Date of birth is required");
        }
        try {
            java.time.LocalDate birthDate = java.time.LocalDate.parse(dateOfBirth, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            java.time.LocalDate minDate = java.time.LocalDate.now().minusYears(150);
            if (birthDate.isBefore(minDate)) {
                throw new IllegalArgumentException("Date of birth cannot be more than 150 years ago");
            }
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use DD/MM/YYYY");
        }

        // Validar género
        if (gender == null || gender.trim().isEmpty()) {
            throw new IllegalArgumentException("Gender is required");
        }
        try {
            Gender.valueOf(gender.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid gender. Must be MASCULINO, FEMENINO, or OTRO");
        }

        // Validar dirección
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }

        // Validar teléfono
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone is required");
        }
        if (!phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone must contain exactly 10 digits");
        }

        // Validar email (opcional)
        if (email != null && !email.trim().isEmpty()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }

        // Validar contacto de emergencia
        if (emergencyName == null || emergencyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact name is required");
        }
        if (emergencyRelation == null || emergencyRelation.trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact relation is required");
        }
        if (emergencyPhone == null || emergencyPhone.trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact phone is required");
        }
        if (!emergencyPhone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Emergency contact phone must contain exactly 10 digits");
        }

        // Validar seguro médico
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Insurance company name is required");
        }
        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Policy number is required");
        }
        if (validityDate == null || validityDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Policy validity date is required");
        }
        try {
            java.time.LocalDate.parse(validityDate, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid policy validity date format. Use DD/MM/YYYY");
        }
    }

    public java.util.List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}