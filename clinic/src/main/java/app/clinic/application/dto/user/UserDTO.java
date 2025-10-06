package app.clinic.application.dto.user;

/**
 * Data Transfer Object for User information.
 * Used for API responses containing user data.
 */
public class UserDTO {
    private String cedula;
    private String username;
    private String fullName;
    private String birthDate;
    private String address;
    private String phoneNumber;
    private String email;
    private String role;
    private boolean active;
    private int age;

    // Default constructor
    public UserDTO() {}

    // Constructor with parameters
    public UserDTO(String cedula, String username, String fullName, String birthDate,
                   String address, String phoneNumber, String email, String role,
                   boolean active, int age) {
        this.cedula = cedula;
        this.username = username;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
        this.active = active;
        this.age = age;
    }

    // Getters and Setters
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("UserDTO{cedula='%s', username='%s', fullName='%s', role='%s', active=%s}",
                           cedula, username, fullName, role, active);
    }
}