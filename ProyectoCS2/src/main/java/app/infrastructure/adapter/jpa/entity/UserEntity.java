package app.infrastructure.adapter.jpa.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(length = 30)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private app.domain.model.Role role;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "version")
    @jakarta.persistence.Version
    private Long version;

    // Constructor por defecto requerido por JPA
    public UserEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor con parámetros
    public UserEntity(String idCard, String fullName, String email, String phone,
                     LocalDate birthDate, String address, app.domain.model.Role role,
                     String username, String password) {
        this();
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

    // Getters and setters con auditoría automática
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) {
        this.fullName = fullName;
        this.updatedAt = LocalDateTime.now();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) {
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        this.updatedAt = LocalDateTime.now();
    }

    public String getAddress() { return address; }
    public void setAddress(String address) {
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }

    public app.domain.model.Role getRole() { return role; }
    public void setRole(app.domain.model.Role role) {
        this.role = role;
        this.updatedAt = LocalDateTime.now();
    }

    public String getUsername() { return username; }
    public void setUsername(String username) {
        this.username = username;
        this.updatedAt = LocalDateTime.now();
    }
    public String getPassword() { return password; }
    public void setPassword(String password) {
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}