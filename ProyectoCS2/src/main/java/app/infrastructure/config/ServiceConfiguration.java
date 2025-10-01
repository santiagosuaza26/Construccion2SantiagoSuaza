package app.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import app.domain.factory.OrderFactory;
import app.domain.port.ClinicalHistoryRepository;
import app.domain.port.DiagnosticTestRepository;
import app.domain.port.MedicationRepository;
import app.domain.port.OrderHeaderRepository;
import app.domain.port.OrderItemRepository;
import app.domain.port.PatientRepository;
import app.domain.port.ProcedureTypeRepository;
import app.domain.port.SpecialtyRepository;
import app.domain.port.UserRepository;
import app.domain.services.AdministrativeService;
import app.domain.services.AuthenticationService;
import app.domain.services.BillingService;
import app.domain.services.DoctorService;
import app.domain.services.HumanResourcesService;
import app.domain.services.InsuranceCalculationService;
import app.domain.services.InventoryService;
import app.domain.services.NurseService;
import app.domain.services.OrderValidationService;
import app.domain.services.PatientValidationService;
import app.domain.services.PatientVisitService;

/**
 * Configuración de servicios que demuestra el cumplimiento de DIP
 * Los servicios de dominio dependen de interfaces (puertos) no de implementaciones concretas
 */
@Configuration
public class ServiceConfiguration {

    /**
     * Configuración de servicios para el perfil de desarrollo
     * Incluye logging detallado y validaciones adicionales
     */
    @Configuration
    @Profile({"dev", "default"})
    public static class DevelopmentServiceConfiguration {

        @Bean
        public PatientValidationService patientValidationService(PatientRepository patientRepository) {
            PatientValidationService service = new PatientValidationService(patientRepository);

            // En desarrollo, agregar logging detallado
            System.out.println("PatientValidationService configurado para desarrollo con logging detallado");

            return service;
        }

        @Bean
        public AdministrativeService administrativeService(PatientRepository patientRepository) {
            return new AdministrativeService(patientRepository);
        }

        @Bean
        public AuthenticationService authenticationService(UserRepository userRepository,
                                                            PatientRepository patientRepository) {
            return new AuthenticationService(userRepository, patientRepository);
        }

        @Bean
        public PatientVisitService patientVisitService(app.domain.port.PatientVisitRepository patientVisitRepository) {
            return new PatientVisitService(patientVisitRepository);
        }

        @Bean
        public OrderFactory orderFactory(OrderHeaderRepository orderHeaderRepository) {
            return new OrderFactory(orderHeaderRepository);
        }

        @Bean
        public HumanResourcesService humanResourcesService(UserRepository userRepository) {
            return new HumanResourcesService(userRepository);
        }

        @Bean
        public InsuranceCalculationService insuranceCalculationService() {
            return new InsuranceCalculationService();
        }

        @Bean
        public BillingService billingService(app.domain.port.InvoiceRepository invoiceRepository,
                                            app.domain.port.PatientRepository patientRepository) {
            return new BillingService(invoiceRepository, patientRepository);
        }

        @Bean
        public InventoryService inventoryService(MedicationRepository medicationRepository,
                                                ProcedureTypeRepository procedureTypeRepository,
                                                DiagnosticTestRepository diagnosticTestRepository,
                                                SpecialtyRepository specialtyRepository) {
            return new InventoryService(medicationRepository, procedureTypeRepository,
                                        diagnosticTestRepository, specialtyRepository);
        }

        @Bean
        public OrderValidationService orderValidationService() {
            return new OrderValidationService();
        }

        @Bean
        public NurseService nurseService(ClinicalHistoryRepository clinicalHistoryRepository) {
            return new NurseService(clinicalHistoryRepository);
        }

        @Bean
        public DoctorService doctorService(OrderHeaderRepository orderHeaderRepository,
                                             OrderItemRepository orderItemRepository,
                                             ClinicalHistoryRepository clinicalHistoryRepository,
                                             MedicationRepository medicationRepository,
                                             ProcedureTypeRepository procedureTypeRepository,
                                             DiagnosticTestRepository diagnosticTestRepository,
                                             SpecialtyRepository specialtyRepository,
                                             OrderValidationService orderValidationService) {

            System.out.println("Configurando DoctorService con repositorios disponibles");
            return new DoctorService(orderHeaderRepository, orderItemRepository, clinicalHistoryRepository,
                                     medicationRepository, procedureTypeRepository, diagnosticTestRepository,
                                     specialtyRepository, orderValidationService);
        }
    }

    /**
     * Configuración de servicios para el perfil de producción
     * Optimizada para performance y seguridad
     */
    @Configuration
    @Profile("prod")
    public static class ProductionServiceConfiguration {

        @Bean
        public PatientValidationService patientValidationService(PatientRepository patientRepository) {
            PatientValidationService service = new PatientValidationService(patientRepository);

            // En producción, configuración optimizada
            System.out.println("PatientValidationService configurado para producción");

            return service;
        }

        @Bean
        public AdministrativeService administrativeService(PatientRepository patientRepository) {
            return new AdministrativeService(patientRepository);
        }

        @Bean
        public AuthenticationService authenticationService(UserRepository userRepository,
                                                            PatientRepository patientRepository) {
            return new AuthenticationService(userRepository, patientRepository);
        }

        @Bean
        public PatientVisitService patientVisitService(app.domain.port.PatientVisitRepository patientVisitRepository) {
            return new PatientVisitService(patientVisitRepository);
        }

        @Bean
        public OrderFactory orderFactory(OrderHeaderRepository orderHeaderRepository) {
            return new OrderFactory(orderHeaderRepository);
        }

        @Bean
        public HumanResourcesService humanResourcesService(UserRepository userRepository) {
            return new HumanResourcesService(userRepository);
        }
    }

    /**
     * Configuración de servicios para el perfil de pruebas
     * Con mocks y datos de prueba
     */
    @Configuration
    @Profile("test")
    public static class TestServiceConfiguration {

        @Bean
        public PatientValidationService patientValidationService(PatientRepository patientRepository) {
            PatientValidationService service = new PatientValidationService(patientRepository);

            // En pruebas, configuración mínima
            System.out.println("PatientValidationService configurado para pruebas");

            return service;
        }

        @Bean
        public AdministrativeService administrativeService(PatientRepository patientRepository) {
            return new AdministrativeService(patientRepository);
        }

        @Bean
        public AuthenticationService authenticationService(UserRepository userRepository,
                                                             PatientRepository patientRepository) {
            return new AuthenticationService(userRepository, patientRepository);
        }

        @Bean
        public PatientVisitService patientVisitService(app.domain.port.PatientVisitRepository patientVisitRepository) {
            return new PatientVisitService(patientVisitRepository);
        }

        @Bean
        public OrderFactory orderFactory(OrderHeaderRepository orderHeaderRepository) {
            return new OrderFactory(orderHeaderRepository);
        }

        @Bean
        public HumanResourcesService humanResourcesService(UserRepository userRepository) {
            return new HumanResourcesService(userRepository);
        }
    }
}