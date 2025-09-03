package app.application.dto;


import app.domain.model.User;

public class UserResponse {
    public String fullName;
    public String idCard;
    public String email;
    public String role;

    public static UserResponse fromDomain(User user) {
        UserResponse dto = new UserResponse();
        dto.fullName = user.getFullName();
        dto.idCard = user.getIdCard();
        dto.email = user.getEmail();
        dto.role = user.getRole().name();
        return dto;
    }
}