package app.application.dto;

import app.domain.model.Credentials;
import app.domain.model.Role;
import app.domain.model.User;

import java.time.LocalDate;

public class UserRequest {
    public String fullName;
    public String idCard;
    public String email;
    public String phone;
    public LocalDate birthDate;
    public String address;
    public Role role;
    public String username;
    public String password;

    public User toDomain() {
        return new User(fullName, idCard, email, phone, birthDate, address, role, new Credentials(username, password));
    }
}