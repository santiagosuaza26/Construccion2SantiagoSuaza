package app.domain.model;

public final class Credentials {
    private final String username;
    private final String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    // Validaciones removidas del constructor - ahora son responsabilidad de servicios específicos
    // Esto permite crear objetos Credentials para testing sin lanzar excepciones prematuras
}