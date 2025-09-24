package app.domain.model;

public final class Credentials {
    private final String username;
    private final String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
        validate();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    private void validate() {
        if (username == null || username.isBlank() || username.length() > 15) {
            throw new IllegalArgumentException("Invalid username");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (!password.matches(".*[A-Z].*") ||
            !password.matches(".*\\d.*") ||
            !password.matches(".*[^A-Za-z0-9].*")) {
            throw new IllegalArgumentException("Password must contain uppercase, number and special char");
        }
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Username must contain only letters and numbers");
        }
    }
}