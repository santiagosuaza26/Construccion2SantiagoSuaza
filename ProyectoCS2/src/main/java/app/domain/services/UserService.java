package app.domain.services;

public class UserService {

    public boolean validatePasswordStrength(String password) {
        return password.length() >= 8 &&
        password.matches(".*[A-Z].*") &&
        password.matches(".*[0-9].*") &&
        password.matches(".*[!@#$%^&*()].*");
    }
}