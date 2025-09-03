package app.domain.model;

import java.time.LocalDate;

public class Patient {
    private final String idCard; // unique
    private final String fullName;
    private final LocalDate birthDate;
    private final String gender;
    private final String address;
    private final String phone;
    private final String email;
    private final Credentials credentials;
    private final EmergencyContact emergencyContact; // optional (min/max 1)
    private final InsurancePolicy insurancePolicy;   // optional (0..1)

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
        if (idCard == null || !idCard.matches("\\d+")) throw new IllegalArgumentException("Invalid idCard");
        if (phone == null || !phone.matches("\\d{10}")) throw new IllegalArgumentException("Invalid patient phone");
        if (birthDate == null) throw new IllegalArgumentException("Birth date required");
    }
}