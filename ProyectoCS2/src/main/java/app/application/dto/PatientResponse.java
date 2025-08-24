package app.application.dto;

import java.time.LocalDate;

public class PatientResponse {
    private String id;
    private String fullName;
    private LocalDate birthDate;
    private String gender;
    private String address;
    private String phoneNumber;
    private String email;

    public PatientResponse(String id, String fullName, LocalDate birthDate, String gender,
        String address, String phoneNumber, String email) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // Getters
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
}
