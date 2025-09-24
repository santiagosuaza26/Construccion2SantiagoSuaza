package app.domain.model;

import java.time.LocalDate;
import java.time.Period;

public class User {
    private final String fullName;
    private final String idCard;
    private final String email;
    private final String phone;
    private final LocalDate birthDate;
    private final String address;
    private final Role role;
    private final Credentials credentials;

    public User(String fullName,
                String idCard,
                String email,
                String phone,
                LocalDate birthDate,
                String address,
                Role role,
                Credentials credentials) {
        this.fullName = fullName;
        this.idCard = idCard;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.role = role;
        this.credentials = credentials;
        validate();
    }

    public String getFullName() { return fullName; }
    public String getIdCard() { return idCard; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getAddress() { return address; }
    public Role getRole() { return role; }
    public Credentials getCredentials() { return credentials; }

    private void validate() {
        if (idCard == null || !idCard.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid idCard - must contain only numbers and be unique");
        }
        
        // Email debe ser válido y contener dominio válido
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email required");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format - must contain valid domain and @");
        }
        
        // Teléfono debe contener entre 1 y 10 dígitos según documento
        if (phone == null || !phone.matches("\\d{1,10}")) {
            throw new IllegalArgumentException("Phone must contain between 1 and 10 digits");
        }
        
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid birth date");
        }
        
        // Validar edad máxima 150 años
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age > 150) {
            throw new IllegalArgumentException("Invalid age: maximum 150 years");
        }
        
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name required");
        }
        
        if (role == null) {
            throw new IllegalArgumentException("Role required");
        }
        
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials required");
        }
        
        // Dirección máximo 30 caracteres
        if (address != null && address.length() > 30) {
            throw new IllegalArgumentException("Address must be maximum 30 characters");
        }
        
        // Validar que Recursos Humanos no pueda ser paciente
        if (role == Role.PATIENT) {
            throw new IllegalArgumentException("Patients should use Patient entity, not User entity");
        }
    }
}