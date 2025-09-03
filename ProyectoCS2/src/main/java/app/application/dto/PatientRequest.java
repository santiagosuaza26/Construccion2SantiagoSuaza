package app.application.dto;
import app.domain.model.*;

import java.time.LocalDate;

public class PatientRequest {
    public String idCard;
    public String fullName;
    public LocalDate birthDate;
    public String gender;
    public String address;
    public String phone;
    public String email;
    public String username;
    public String password;
    public EmergencyContact emergencyContact;
    public InsurancePolicy insurancePolicy;

    public Patient toDomain() {
        return new Patient(idCard, fullName, birthDate, gender, address, phone, email,
                new Credentials(username, password), emergencyContact, insurancePolicy);
    }
}