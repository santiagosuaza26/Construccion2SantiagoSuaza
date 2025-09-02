package app.domain.model;

import java.time.LocalDate;

public class User {
    private final String fullName;
    private final String idCard; // unique
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
        if (idCard == null || !idCard.matches("\\d+")) throw new IllegalArgumentException("Invalid idCard");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid email");
        if (phone == null || !phone.matches("\\d{1,10}")) throw new IllegalArgumentException("Invalid phone");
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) throw new IllegalArgumentException("Invalid birthDate");
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("Invalid fullName");
        if (role == null) throw new IllegalArgumentException("Role required");
    }
}