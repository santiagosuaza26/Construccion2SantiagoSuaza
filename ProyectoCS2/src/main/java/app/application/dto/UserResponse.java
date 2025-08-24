package app.application.dto;

public class UserResponse {
    private String id;
    private String fullName;
    private String email;
    private String userName;

    public UserResponse(String id, String fullName, String email, String userName) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.userName = userName;
    }

    // Getters
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getUserName() { return userName; }
}
