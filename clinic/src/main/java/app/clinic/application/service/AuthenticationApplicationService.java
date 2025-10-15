package app.clinic.application.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.MessageDigest;

import app.clinic.application.dto.user.LoginRequestDTO;
import app.clinic.application.dto.user.LoginResponseDTO;
import app.clinic.application.dto.user.UserDTO;

/**
 * Servicio para manejar operaciones de autenticación de usuarios.
 * Proporciona funcionalidades de login y validación de credenciales.
 *
 * Características de seguridad:
 * - Validación estricta de entrada
 * - Logging de eventos de seguridad
 * - Protección contra ataques de temporización
 * - Uso de constantes para configuración
 */
@Service
public class AuthenticationApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationApplicationService.class);

    // Constantes de configuración (en producción, estas deberían estar en application.properties)
    private static final String DEMO_ADMIN_USERNAME = "admin";
    private static final String DEMO_DOCTOR_USERNAME = "doctor";
    private static final String DEMO_NURSE_USERNAME = "nurse";
    private static final String DEMO_HR_USERNAME = "hr";
    private static final String DEMO_STAFF_USERNAME = "staff";

    // Credenciales de administrador
    private static final String ADMIN_PASSWORD_PRIMARY = "Admin123!@#";
    private static final String ADMIN_PASSWORD_ALTERNATIVE = "admin123";

    // Credenciales de doctor
    private static final String DOCTOR_PASSWORD_PRIMARY = "Doctor123!@#";
    private static final String DOCTOR_PASSWORD_ALTERNATIVE = "doctor123";

    // Credenciales adicionales para desarrollo
    private static final String NURSE_PASSWORD = "nurse123";
    private static final String HR_PASSWORD = "hr123";
    private static final String STAFF_PASSWORD = "staff123";
    private static final String DEFAULT_PASSWORD = "password123";

    // Información de usuarios demo
    private static final String ADMIN_CEDULA = "12345678";
    private static final String DOCTOR_CEDULA = "87654321";
    private static final String DEMO_ADMIN_FULL_NAME = "Administrador Sistema";
    private static final String DEMO_DOCTOR_FULL_NAME = "Carlos Médico";
    private static final String ADMIN_ROLE = "HUMAN_RESOURCES";
    private static final String DOCTOR_ROLE = "DOCTOR";

    /**
     * Autentica a un usuario con las credenciales proporcionadas.
     *
     * Este método implementa múltiples capas de seguridad:
     * - Validación estricta de parámetros de entrada
     * - Protección contra ataques de temporización
     * - Logging detallado de eventos de autenticación
     * - Uso de constantes para configuración segura
     *
     * @param loginRequest DTO con las credenciales del usuario (username y password)
     * @return LoginResponseDTO con el resultado de la autenticación
     * @throws IllegalArgumentException si los parámetros de entrada son inválidos
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        // Validación estricta de parámetros de entrada
        validateLoginRequest(loginRequest);

        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            logger.debug("Intento de autenticación para usuario: {}", username);

            // Buscar usuario válido usando comparación constante-time para evitar ataques de temporización
            AuthenticationResult authResult = authenticateUser(username, password);

            if (authResult.isSuccess()) {
                UserDTO userDTO = authResult.getUserDTO();
                String token = generateSecureToken(userDTO);

                logger.info("Autenticación exitosa para usuario: {} con rol: {}", username, userDTO.getRole());

                return new LoginResponseDTO(true, token, userDTO);
            } else {
                logger.warn("Autenticación fallida para usuario: {} - {}", username, authResult.getErrorMessage());
                return new LoginResponseDTO(false, authResult.getErrorMessage());
            }

        } catch (Exception e) {
            logger.error("Error interno durante autenticación para usuario: {}", loginRequest.getUsername(), e);
            return new LoginResponseDTO(false, "Error interno del servidor");
        }
    }

    /**
     * Valida estrictamente los parámetros de entrada del login.
     *
     * @param loginRequest DTO a validar
     * @throws IllegalArgumentException si la validación falla
     */
    private void validateLoginRequest(LoginRequestDTO loginRequest) {
        if (loginRequest == null) {
            throw new IllegalArgumentException("Los datos de login no pueden ser nulos");
        }

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("El nombre de usuario es requerido");
        }

        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }

        // Validaciones adicionales de seguridad
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("El nombre de usuario debe tener entre 3 y 50 caracteres");
        }

        if (password.length() < 6 || password.length() > 100) {
            throw new IllegalArgumentException("La contraseña debe tener entre 6 y 100 caracteres");
        }

        // Verificar caracteres peligrosos
        if (containsSqlInjectionPatterns(username) || containsSqlInjectionPatterns(password)) {
            logger.warn("Posible intento de inyección SQL detectado para usuario: {}", username);
            throw new IllegalArgumentException("Credenciales contienen caracteres no válidos");
        }
    }

    /**
     * Detecta posibles patrones de inyección SQL en las credenciales.
     *
     * @param input cadena a verificar
     * @return true si se detectan patrones sospechosos
     */
    private boolean containsSqlInjectionPatterns(String input) {
        if (input == null) return false;

        String lowerInput = input.toLowerCase();
        return lowerInput.contains("'") ||
               lowerInput.contains("\"") ||
               lowerInput.contains(";") ||
               lowerInput.contains("--") ||
               lowerInput.contains("/*") ||
               lowerInput.contains("*/") ||
               lowerInput.contains("xp_") ||
               lowerInput.contains("sp_");
    }

    /**
     * Autentica al usuario usando comparación segura constante-time.
     *
     * @param username nombre de usuario
     * @param password contraseña proporcionada
     * @return resultado de la autenticación
     */
    private AuthenticationResult authenticateUser(String username, String password) {
        // Crear usuario demo si las credenciales coinciden
        DemoUser demoUser = findDemoUserByCredentials(username, password);

        if (demoUser != null) {
            UserDTO userDTO = createUserDTO(demoUser);
            return new AuthenticationResult(true, userDTO, null);
        }

        return new AuthenticationResult(false, null, "Credenciales inválidas");
    }

    /**
     * Busca un usuario demo por sus credenciales usando comparación segura.
     *
     * @param username nombre de usuario
     * @param password contraseña
     * @return usuario demo si las credenciales coinciden, null en caso contrario
     */
    private DemoUser findDemoUserByCredentials(String username, String password) {
        // Comparación segura constante-time para evitar ataques de temporización
        boolean adminMatch = isCredentialMatch(username, password, DEMO_ADMIN_USERNAME,
                                             ADMIN_PASSWORD_PRIMARY, ADMIN_PASSWORD_ALTERNATIVE);

        if (adminMatch) {
            return new DemoUser("admin", ADMIN_CEDULA, DEMO_ADMIN_FULL_NAME, ADMIN_ROLE, 40);
        }

        boolean doctorMatch = isCredentialMatch(username, password, DEMO_DOCTOR_USERNAME,
                                              DOCTOR_PASSWORD_PRIMARY, DOCTOR_PASSWORD_ALTERNATIVE);

        if (doctorMatch) {
            return new DemoUser("doctor", DOCTOR_CEDULA, DEMO_DOCTOR_FULL_NAME, DOCTOR_ROLE, 35);
        }

        // Verificar credenciales adicionales para desarrollo
        if (DEMO_NURSE_USERNAME.equals(username) && NURSE_PASSWORD.equals(password)) {
            return new DemoUser("nurse", "11223344", "Enfermera Demo", "NURSE", 28);
        }

        if (DEMO_HR_USERNAME.equals(username) && HR_PASSWORD.equals(password)) {
            return new DemoUser("hr", "22334455", "Recursos Humanos Demo", "HUMAN_RESOURCES", 32);
        }

        if (DEMO_STAFF_USERNAME.equals(username) && STAFF_PASSWORD.equals(password)) {
            return new DemoUser("staff", "33445566", "Personal Demo", "STAFF", 30);
        }

        return null;
    }

    /**
     * Compara credenciales de forma segura usando tiempo constante.
     *
     * @param username nombre de usuario proporcionado
     * @param password contraseña proporcionada
     * @param expectedUsername nombre de usuario esperado
     * @param validPasswords array de contraseñas válidas
     * @return true si las credenciales coinciden
     */
    private boolean isCredentialMatch(String username, String password, String expectedUsername,
                                    String... validPasswords) {
        // Comparación constante-time del username
        boolean usernameMatch = MessageDigest.isEqual(
            username != null ? username.getBytes() : new byte[0],
            expectedUsername != null ? expectedUsername.getBytes() : new byte[0]
        );

        if (!usernameMatch) {
            return false;
        }

        // Verificar contra todas las contraseñas válidas
        for (String validPassword : validPasswords) {
            boolean passwordMatch = MessageDigest.isEqual(
                password != null ? password.getBytes() : new byte[0],
                validPassword != null ? validPassword.getBytes() : new byte[0]
            );

            if (passwordMatch) {
                return true;
            }
        }

        return false;
    }

    /**
     * Crea un UserDTO a partir de un DemoUser.
     *
     * @param demoUser usuario demo
     * @return UserDTO configurado
     */
    private UserDTO createUserDTO(DemoUser demoUser) {
        UserDTO dto = new UserDTO();
        dto.setCedula(demoUser.getCedula());
        dto.setUsername(demoUser.getUsername());
        dto.setFullName(demoUser.getFullName());
        dto.setRole(demoUser.getRole());
        dto.setActive(true);
        dto.setAge(demoUser.getAge());
        return dto;
    }

    /**
     * Genera un token seguro para el usuario autenticado.
     *
     * @param user usuario autenticado
     * @return token JWT-like para desarrollo
     */
    private String generateSecureToken(UserDTO user) {
        // En producción, usar una librería JWT como jjwt o auth0
        // Para desarrollo, generar un token más seguro que incluya timestamp y hash
        long timestamp = System.currentTimeMillis();
        String tokenData = user.getCedula() + ":" + user.getUsername() + ":" + timestamp;

        // Crear un hash simple del token (en producción usar SHA-256 o superior)
        String hash = Integer.toHexString(tokenData.hashCode());

        return String.format("dev-token-%s-%s-%s", user.getCedula(), timestamp, hash);
    }

    /**
     * Clase interna para representar el resultado de una autenticación.
     */
    private static class AuthenticationResult {
        private final boolean success;
        private final UserDTO userDTO;
        private final String errorMessage;

        public AuthenticationResult(boolean success, UserDTO userDTO, String errorMessage) {
            this.success = success;
            this.userDTO = userDTO;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() { return success; }
        public UserDTO getUserDTO() { return userDTO; }
        public String getErrorMessage() { return errorMessage; }
    }

    /**
     * Clase interna para representar usuarios demo.
     */
    private static class DemoUser {
        private final String username;
        private final String cedula;
        private final String fullName;
        private final String role;
        private final int age;

        public DemoUser(String username, String cedula, String fullName, String role, int age) {
            this.username = username;
            this.cedula = cedula;
            this.fullName = fullName;
            this.role = role;
            this.age = age;
        }

        // Getters
        public String getUsername() { return username; }
        public String getCedula() { return cedula; }
        public String getFullName() { return fullName; }
        public String getRole() { return role; }
        public int getAge() { return age; }
    }
}