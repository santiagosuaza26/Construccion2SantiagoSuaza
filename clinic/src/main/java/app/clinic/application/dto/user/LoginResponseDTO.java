package app.clinic.application.dto.user;

/**
 * DTO for user login responses.
 * Contains the authentication result and user information.
 */
public class LoginResponseDTO {

    private boolean success;
    private String token;
    private UserDTO user;
    private String message;

    // Default constructor
    public LoginResponseDTO() {}

    // Constructor for successful login
    public LoginResponseDTO(boolean success, String token, UserDTO user) {
        this.success = success;
        this.token = token;
        this.user = user;
        this.message = success ? "Login successful" : "Login failed";
    }

    // Constructor for failed login
    public LoginResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LoginResponseDTO{" +
                "success=" + success +
                ", token='" + (token != null ? "[PROTECTED]" : "null") + '\'' +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", message='" + message + '\'' +
                '}';
    }
}