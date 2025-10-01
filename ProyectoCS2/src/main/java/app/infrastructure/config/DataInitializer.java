package app.infrastructure.config;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import app.application.dto.request.CreateUserRequest;
import app.application.dto.request.InventoryItemRequest;
import app.application.dto.request.RegisterPatientRequest;
import app.application.service.InventoryApplicationService;
import app.application.service.PatientApplicationService;
import app.application.service.UserApplicationService;
import app.domain.model.EmergencyContact;
import app.domain.model.InsurancePolicy;
import app.domain.model.Role;
import app.domain.services.AuthenticationService.AuthenticatedUser;

/**
 * Inicializador de datos de prueba
 *
 * Crea datos de prueba para todos los módulos cuando la aplicación
 * se ejecuta en modo desarrollo o testing.
 *
 * Datos creados:
 * - Usuarios de prueba para todos los roles
 * - Pacientes de prueba
 * - Medicamentos, procedimientos y ayudas diagnósticas
 * - Órdenes médicas de prueba
 * - Facturas de prueba
 */
@Component
@Profile({"dev", "test"})
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserApplicationService userApplicationService;
    private final PatientApplicationService patientApplicationService;
    private final InventoryApplicationService inventoryApplicationService;

    public DataInitializer(UserApplicationService userApplicationService,
                          PatientApplicationService patientApplicationService,
                          InventoryApplicationService inventoryApplicationService) {
        this.userApplicationService = userApplicationService;
        this.patientApplicationService = patientApplicationService;
        this.inventoryApplicationService = inventoryApplicationService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Inicializando datos de prueba...");

        try {
            // Crear usuarios autenticados mock según las reglas de negocio
            AuthenticatedUser adminUser = new AuthenticatedUser("00000000", "Admin System", Role.ADMINISTRATIVE, true);
            AuthenticatedUser supportUser = new AuthenticatedUser("11111111", "Support System", Role.SUPPORT, true);

            // Inicializar datos en orden con usuarios apropiados según permisos
            initializeUsers(adminUser);
            initializePatients(adminUser);
            initializeInventory(supportUser); // Usar usuario SUPPORT para inventario

            logger.info("Datos de prueba inicializados exitosamente");

        } catch (Exception e) {
            logger.error("Error inicializando datos de prueba", e);
        }
    }

    /**
     * Inicializar usuarios de prueba para todos los roles
     */
    private void initializeUsers(AuthenticatedUser adminUser) {
        logger.info("Creando usuarios de prueba...");

        // Usuario Recursos Humanos
        CreateUserRequest hrUser = new CreateUserRequest();
        hrUser.setFullName("Carlos Ramírez");
        hrUser.setIdCard("12345678");
        hrUser.setEmail("carlos.ramirez@clinica.cs2");
        hrUser.setPhone("3001234567");
        hrUser.setBirthDate("15/03/1980");
        hrUser.setAddress("Calle 123 #45-67");
        hrUser.setRole("HUMAN_RESOURCES");
        hrUser.setUsername("carlos123");
        hrUser.setPassword("Carlos123!");
        userApplicationService.createUser(hrUser);

        // Usuario Administrativo
        CreateUserRequest adminUserRequest = new CreateUserRequest();
        adminUserRequest.setFullName("María González");
        adminUserRequest.setIdCard("87654321");
        adminUserRequest.setEmail("maria.gonzalez@clinica.cs2");
        adminUserRequest.setPhone("3109876543");
        adminUserRequest.setBirthDate("22/07/1985");
        adminUserRequest.setAddress("Carrera 67 #89-01");
        adminUserRequest.setRole("ADMINISTRATIVE");
        adminUserRequest.setUsername("maria123");
        adminUserRequest.setPassword("Maria123!");
        userApplicationService.createUser(adminUserRequest);

        // Usuario Soporte de Información
        CreateUserRequest supportUser = new CreateUserRequest();
        supportUser.setFullName("Jorge Martínez");
        supportUser.setIdCard("11223344");
        supportUser.setEmail("jorge.martinez@clinica.cs2");
        supportUser.setPhone("3204567890");
        supportUser.setBirthDate("08/11/1982");
        supportUser.setAddress("Avenida 45 #23-56");
        supportUser.setRole("SUPPORT");
        supportUser.setUsername("jorge123");
        supportUser.setPassword("Jorge123!");
        userApplicationService.createUser(supportUser);

        // Usuario Médico
        CreateUserRequest doctorUser = new CreateUserRequest();
        doctorUser.setFullName("Ana López");
        doctorUser.setIdCard("55566677");
        doctorUser.setEmail("ana.lopez@clinica.cs2");
        doctorUser.setPhone("3005678901");
        doctorUser.setBirthDate("30/05/1978");
        doctorUser.setAddress("Diagonal 78 #12-34");
        doctorUser.setRole("DOCTOR");
        doctorUser.setUsername("ana123");
        doctorUser.setPassword("Ana123!");
        userApplicationService.createUser(doctorUser);

        // Usuario Enfermera
        CreateUserRequest nurseUser = new CreateUserRequest();
        nurseUser.setFullName("Lucía Herrera");
        nurseUser.setIdCard("99988877");
        nurseUser.setEmail("lucia.herrera@clinica.cs2");
        nurseUser.setPhone("3102345678");
        nurseUser.setBirthDate("12/09/1990");
        nurseUser.setAddress("Transversal 34 #56-78");
        nurseUser.setRole("NURSE");
        nurseUser.setUsername("lucia123");
        nurseUser.setPassword("Lucia123!");
        userApplicationService.createUser(nurseUser);

        logger.info("Usuarios de prueba creados exitosamente");
    }

    /**
     * Inicializar pacientes de prueba
     */
    private void initializePatients(AuthenticatedUser adminUser) {
        logger.info("Creando pacientes de prueba...");

        // Paciente 1
        EmergencyContact emergencyContact1 = new EmergencyContact(
            "María", "González", "Hermana", "3001234567"
        );

        InsurancePolicy insurance1 = new InsurancePolicy(
            "EPS001", "Salud Total", true, LocalDate.now().plusDays(300)
        );

        RegisterPatientRequest patient1 = new RegisterPatientRequest(
            "10000001", "Juan Pérez García", "15/06/1985", "Masculino",
            "3001234567", "juan.perez@email.com", "juan123", "Juan123!",
            "María", "González", "Hermana", "3001234567"
        );
        patient1.setAddress("Calle 123 #45-67, Bogotá");
        patient1.setInsuranceCompany("EPS001");
        patient1.setPolicyNumber("Salud Total");
        patient1.setPolicyActive(true);
        patient1.setPolicyEndDate("15/06/2025");
        patientApplicationService.registerPatient(patient1, adminUser);

        // Paciente 2
        EmergencyContact emergencyContact2 = new EmergencyContact(
            "Carlos", "Rodríguez", "Esposo", "3109876543"
        );

        InsurancePolicy insurance2 = new InsurancePolicy(
            "EPS002", "Compensar", true, LocalDate.now().plusDays(180)
        );

        RegisterPatientRequest patient2 = new RegisterPatientRequest(
            "20000002", "María Fernanda López", "08/12/1992", "Femenino",
            "3109876543", "maria.lopez@email.com", "maria123", "Maria123!",
            "Carlos", "Rodríguez", "Esposo", "3109876543"
        );
        patient2.setAddress("Carrera 67 #89-01, Bogotá");
        patient2.setInsuranceCompany("EPS002");
        patient2.setPolicyNumber("Compensar");
        patient2.setPolicyActive(true);
        patient2.setPolicyEndDate("08/12/2024");
        patientApplicationService.registerPatient(patient2, adminUser);

        // Paciente 3 (sin seguro)
        EmergencyContact emergencyContact3 = new EmergencyContact(
            "Pedro", "Ramírez", "Padre", "3204567890"
        );

        RegisterPatientRequest patient3 = new RegisterPatientRequest(
            "30000003", "Carmen Rodríguez", "25/03/1970", "Femenino",
            "3204567890", "carmen.rodriguez@email.com", "carmen123", "Carmen123!",
            "Pedro", "Ramírez", "Padre", "3204567890"
        );
        patient3.setAddress("Avenida 45 #23-56, Bogotá");
        patientApplicationService.registerPatient(patient3, adminUser);

        logger.info("Pacientes de prueba creados exitosamente");
    }

    /**
     * Inicializar inventario de prueba
     */
    private void initializeInventory(AuthenticatedUser adminUser) {
        logger.info("Creando inventario de prueba...");

        // Medicamentos
        InventoryItemRequest medication1 = new InventoryItemRequest(
            "MEDICATION", "MED001", "Paracetamol 500mg", "Analgésico y antipirético", 15000L, 100
        );
        inventoryApplicationService.createMedication(medication1, adminUser);

        InventoryItemRequest medication2 = new InventoryItemRequest(
            "MEDICATION", "MED002", "Ibuprofeno 400mg", "Antiinflamatorio no esteroideo", 12000L, 50
        );
        inventoryApplicationService.createMedication(medication2, adminUser);

        InventoryItemRequest medication3 = new InventoryItemRequest(
            "MEDICATION", "MED003", "Amoxicilina 500mg", "Antibiótico", 25000L, 30
        );
        inventoryApplicationService.createMedication(medication3, adminUser);

        // Procedimientos
        InventoryItemRequest procedure1 = new InventoryItemRequest(
            "PROCEDURE", "PROC001", "Consulta General", "Consulta médica general", 80000L, 0
        );
        inventoryApplicationService.createProcedure(procedure1, adminUser);

        InventoryItemRequest procedure2 = new InventoryItemRequest(
            "PROCEDURE", "PROC002", "Curación Simple", "Limpieza y curación de herida", 45000L, 0
        );
        inventoryApplicationService.createProcedure(procedure2, adminUser);

        InventoryItemRequest procedure3 = new InventoryItemRequest(
            "PROCEDURE", "PROC003", "Inyección Intramuscular", "Aplicación de medicamento vía IM", 25000L, 0
        );
        inventoryApplicationService.createProcedure(procedure3, adminUser);

        // Ayudas diagnósticas
        InventoryItemRequest diagnostic1 = new InventoryItemRequest(
            "DIAGNOSTIC", "DIAG001", "Hemograma Completo", "Análisis de sangre completo", 35000L, 0
        );
        inventoryApplicationService.createDiagnosticTest(diagnostic1, adminUser);

        InventoryItemRequest diagnostic2 = new InventoryItemRequest(
            "DIAGNOSTIC", "DIAG002", "Radiografía de Tórax", "Imagen diagnóstica de tórax", 60000L, 0
        );
        inventoryApplicationService.createDiagnosticTest(diagnostic2, adminUser);

        InventoryItemRequest diagnostic3 = new InventoryItemRequest(
            "DIAGNOSTIC", "DIAG003", "Glucemia", "Medición de niveles de glucosa", 15000L, 0
        );
        inventoryApplicationService.createDiagnosticTest(diagnostic3, adminUser);

        logger.info("Inventario de prueba creado exitosamente");
    }
}