package app.clinic.integration;

import app.clinic.Cs2Application;
import app.clinic.domain.model.*;
import app.clinic.domain.service.PatientDomainService;
import app.clinic.domain.service.AppointmentDomainService;
import app.clinic.domain.service.BillingDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Test de integración completo para el flujo de paciente.
 * Verifica que todos los servicios críticos funcionan correctamente juntos.
 */
@SpringBootTest(classes = Cs2Application.class)
@ActiveProfiles("test")
@Transactional
@DisplayName("Patient Service Integration Tests")
class PatientServiceIntegrationTest {

    @Autowired
    private PatientDomainService patientService;

    @Autowired
    private AppointmentDomainService appointmentService;

    @Autowired
    private BillingDomainService billingService;

    @Test
    @DisplayName("Debe completar flujo completo de paciente exitosamente")
    void shouldCompleteFullPatientFlowSuccessfully() {
        // Given - Crear paciente básico
        Patient patient = createTestPatient();

        // When - Registrar paciente
        Patient registeredPatient = patientService.registerPatient(patient);

        // Then - Verificar registro
        assertThat(registeredPatient).isNotNull();
        assertThat(registeredPatient.getCedula()).isEqualTo(patient.getCedula());

        // Given - Crear cita médica
        Appointment appointment = createTestAppointment(registeredPatient.getCedula());

        // When - Agendar cita
        Appointment scheduledAppointment = appointmentService.scheduleAppointment(appointment);

        // Then - Verificar agendamiento
        assertThat(scheduledAppointment).isNotNull();
        assertThat(scheduledAppointment.getStatus()).isEqualTo(AppointmentStatus.PROGRAMADA);

        // Given - Calcular costos médicos
        TotalCost serviceCost = TotalCost.of(Money.of(new BigDecimal("150000")));

        // When - Calcular facturación
        BillingCalculationResult billingResult = billingService.calculateBilling(
            registeredPatient.getCedula(), serviceCost);

        // Then - Verificar facturación
        assertThat(billingResult).isNotNull();
        assertThat(billingResult.getTotalCost().getAmount()).isEqualTo(new BigDecimal("150000"));
    }

    @Test
    @DisplayName("Debe manejar múltiples citas para mismo paciente")
    void shouldHandleMultipleAppointmentsForSamePatient() {
        // Given
        Patient patient = createTestPatient();
        Patient registeredPatient = patientService.registerPatient(patient);

        Appointment appointment1 = createTestAppointment(registeredPatient.getCedula());
        Appointment appointment2 = createTestAppointmentForNextDay(registeredPatient.getCedula());

        // When
        Appointment result1 = appointmentService.scheduleAppointment(appointment1);
        Appointment result2 = appointmentService.scheduleAppointment(appointment2);

        // Then
        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result1.getStatus()).isEqualTo(AppointmentStatus.PROGRAMADA);
        assertThat(result2.getStatus()).isEqualTo(AppointmentStatus.PROGRAMADA);
    }

    @Test
    @DisplayName("Debe validar reglas de negocio críticas")
    void shouldValidateCriticalBusinessRules() {
        // Given
        Patient patient = createTestPatient();

        // When & Then - Intentar registrar paciente con datos válidos
        assertThatCode(() -> patientService.registerPatient(patient))
            .doesNotThrowAnyException();

        // Given - Intentar registrar paciente duplicado
        Patient duplicatePatient = createDuplicatePatient();

        // When & Then - Debe lanzar excepción por regla de negocio
        assertThatThrownBy(() -> patientService.registerPatient(duplicatePatient))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Debe mantener integridad referencial entre servicios")
    void shouldMaintainReferentialIntegrityBetweenServices() {
        // Given
        Patient patient = createTestPatient();
        Patient registeredPatient = patientService.registerPatient(patient);

        Appointment appointment = createTestAppointment(registeredPatient.getCedula());
        Appointment scheduledAppointment = appointmentService.scheduleAppointment(appointment);

        // When - Buscar paciente por diferentes criterios
        var foundByCedula = patientService.findPatientByCedula(registeredPatient.getCedula());
        var foundById = patientService.findPatientById(extractPatientId(registeredPatient));

        // Then - Verificar consistencia de datos
        assertThat(foundByCedula).isPresent();
        assertThat(foundById).isPresent();
        assertThat(foundByCedula.get().getCedula()).isEqualTo(foundById.get().getCedula());
    }

    // Métodos auxiliares para crear datos de prueba
    private Patient createTestPatient() {
        return Patient.of(
            PatientCedula.of("12345678"),
            PatientUsername.of("test.patient"),
            PatientPassword.of("password123"),
            PatientFullName.of("Paciente", "Prueba"),
            PatientBirthDate.of(LocalDate.of(1990, 1, 1)),
            PatientGender.MASCULINO,
            PatientAddress.of("Calle de Prueba 123"),
            PatientPhoneNumber.of("3001234567"),
            PatientEmail.of("test@example.com"),
            java.util.List.of(),
            createTestInsurancePolicy()
        );
    }

    private Patient createDuplicatePatient() {
        return Patient.of(
            PatientCedula.of("12345678"), // Mismo número de cédula
            PatientUsername.of("test.patient2"),
            PatientPassword.of("password123"),
            PatientFullName.of("Paciente", "Duplicado"),
            PatientBirthDate.of(LocalDate.of(1990, 1, 1)),
            PatientGender.MASCULINO,
            PatientAddress.of("Calle de Prueba 123"),
            PatientPhoneNumber.of("3001234567"),
            PatientEmail.of("test2@example.com"),
            java.util.List.of(),
            createTestInsurancePolicy()
        );
    }

    private InsurancePolicy createTestInsurancePolicy() {
        return InsurancePolicy.of(
            InsuranceCompanyName.of("Seguros Test"),
            PolicyNumber.of("POL-TEST-001"),
            PolicyStatus.ACTIVA,
            PolicyExpirationDate.of(LocalDate.now().plusMonths(12))
        );
    }

    private Appointment createTestAppointment(PatientCedula patientCedula) {
        return Appointment.of(
            "appointment-1",
            patientCedula,
            DoctorCedula.of("87654321"),
            AppointmentDateTime.of(LocalDateTime.now().plusDays(1)),
            AppointmentStatus.PROGRAMADA,
            ConsultationReason.of("Consulta general de integración")
        );
    }

    private Appointment createTestAppointmentForNextDay(PatientCedula patientCedula) {
        return Appointment.of(
            "appointment-2",
            patientCedula,
            DoctorCedula.of("87654321"),
            AppointmentDateTime.of(LocalDateTime.now().plusDays(2)),
            AppointmentStatus.PROGRAMADA,
            ConsultationReason.of("Seguimiento médico")
        );
    }

    private PatientId extractPatientId(Patient patient) {
        // Extraer ID del paciente basado en su cédula
        return PatientId.of(patient.getCedula().getValue());
    }
}