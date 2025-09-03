package app.adapter.out.persistence.entity;

import java.time.LocalDate;

import app.domain.model.Credentials;
import app.domain.model.Role;
import app.domain.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "id_card")
    private String idCard;
    
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "phone", nullable = false)
    private String phone;
    
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    
    @Column(name = "address", nullable = false, length = 30)
    private String address;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
    
    @Column(name = "username", nullable = false, unique = true, length = 15)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password;

    public UserEntity() {}

    public UserEntity(String idCard, String fullName, String email, String phone, 
                        LocalDate birthDate, String address, Role role, String username, String password) {
        this.idCard = idCard;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public User toDomain() {
        return new User(fullName, idCard, email, phone, birthDate, address, role, 
                        new Credentials(username, password));
    }

    public static UserEntity fromDomain(User user) {
        return new UserEntity(
            user.getIdCard(),
            user.getFullName(),
            user.getEmail(),
            user.getPhone(),
            user.getBirthDate(),
            user.getAddress(),
            user.getRole(),
            user.getCredentials().getUsername(),
            user.getCredentials().getPassword()
        );
    }

    // Getters and setters
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}