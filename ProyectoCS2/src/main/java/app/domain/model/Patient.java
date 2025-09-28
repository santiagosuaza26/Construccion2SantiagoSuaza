package app.domain.model;

import java.time.LocalDate;
import java.time.Period;

public class Patient {
    private final String idCard;
    private final String fullName;
    private final LocalDate birthDate;
    private final String gender;
    private final String address;
    private final String phone;
    private final String email;
    private final Credentials credentials;
    private final EmergencyContact emergencyContact;
    private final InsurancePolicy insurancePolicy;

    public Patient(String idCard,
                String fullName,
                LocalDate birthDate,
                String gender,
                String address,
                String phone,
                String email,
                Credentials credentials,
                EmergencyContact emergencyContact,
                InsurancePolicy insurancePolicy) {
        this.idCard = idCard;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.credentials = credentials;
        this.emergencyContact = emergencyContact;
        this.insurancePolicy = insurancePolicy;
        validate();
    }

    public String getIdCard() { return idCard; }
    public String getFullName() { return fullName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public Credentials getCredentials() { return credentials; }
    public EmergencyContact getEmergencyContact() { return emergencyContact; }
    public InsurancePolicy getInsurancePolicy() { return insurancePolicy; }

    private void validate() {
        if (idCard == null || !idCard.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid idCard - must contain only numbers");
        }

        // Patient phone must be exactly 10 digits
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("El teléfono del paciente debe tener exactamente 10 dígitos");
        }

        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date required");
        }

        // Validate maximum age of 150 years
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age > 150) {
            throw new IllegalArgumentException("Edad inválida: máximo 150 años");
        }

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name required");
        }

        // Enhanced email validation
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email es requerido");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Formato de email inválido - debe contener dominio válido y @");
        }

        if (credentials == null) {
            throw new IllegalArgumentException("Credentials required");
        }

        if (emergencyContact == null) {
            throw new IllegalArgumentException("Emergency contact required");
        }

        // Address maximum 30 characters
        if (address != null && address.length() > 30) {
            throw new IllegalArgumentException("La dirección debe tener máximo 30 caracteres");
        }

        // Validate gender
        if (gender == null || gender.isBlank()) {
            throw new IllegalArgumentException("El género es requerido");
        }
        if (!gender.equalsIgnoreCase("masculino") &&
            !gender.equalsIgnoreCase("femenino") &&
            !gender.equalsIgnoreCase("otro")) {
            throw new IllegalArgumentException("El género debe ser: masculino, femenino, o otro");
        }
    }
}