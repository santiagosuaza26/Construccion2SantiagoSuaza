package app.clinic.domain.service;

import app.clinic.domain.model.*;
import app.clinic.domain.port.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests comprehensivos para AppointmentDomainService.
 * Cubre operaciones de citas médicas, validaciones y reglas de negocio.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AppointmentDomainService Tests")
class AppointmentDomainServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    private AppointmentDomainService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentDomainService(appointmentRepository);
    }

    @Nested
    @DisplayName("Agendamiento de Citas")
    class AppointmentSchedulingTests {

        @Test
        @DisplayName("Debe agendar cita correctamente con datos válidos")
        void shouldScheduleAppointmentSuccessfully() {
            // Given
            Appointment appointment = createValidAppointment();
            when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

            // When
            Appointment result = appointmentService.scheduleAppointment(appointment);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(AppointmentStatus.PROGRAMADA);
            verify(appointmentRepository).save(appointment);
        }

        @Test
        @DisplayName("Debe rechazar cita en el pasado")
        void shouldRejectAppointmentInThePast() {
            // Given
            Appointment pastAppointment = createPastAppointment();

            // When & Then
            assertThatThrownBy(() -> appointmentService.scheduleAppointment(pastAppointment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Appointment cannot be scheduled in the past");
        }

        @Test
        @DisplayName("Debe rechazar cita cuando doctor no está disponible")
        void shouldRejectAppointmentWhenDoctorNotAvailable() {
            // Given
            Appointment appointment = createValidAppointment();
            Appointment conflictingAppointment = createConflictingAppointment();

            when(appointmentRepository.findByDoctorCedula(any(DoctorCedula.class)))
                .thenReturn(List.of(conflictingAppointment));

            // When & Then
            assertThatThrownBy(() -> appointmentService.scheduleAppointment(appointment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Doctor is not available at the requested time");
        }

        @Test
        @DisplayName("Debe permitir múltiples citas para mismo paciente")
        void shouldAllowMultipleAppointmentsForSamePatient() {
            // Given
            Appointment appointment1 = createValidAppointment();
            Appointment appointment2 = createValidAppointmentForSamePatient();

            when(appointmentRepository.findByDoctorCedula(any(DoctorCedula.class)))
                .thenReturn(List.of()); // No conflicts
            when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(appointment1)
                .thenReturn(appointment2);

            // When
            Appointment result1 = appointmentService.scheduleAppointment(appointment1);
            Appointment result2 = appointmentService.scheduleAppointment(appointment2);

            // Then
            assertThat(result1).isNotNull();
            assertThat(result2).isNotNull();
            verify(appointmentRepository, times(2)).save(any(Appointment.class));
        }
    }

    @Nested
    @DisplayName("Actualización de Citas")
    class AppointmentUpdateTests {

        @Test
        @DisplayName("Debe actualizar cita correctamente")
        void shouldUpdateAppointmentSuccessfully() {
            // Given
            Appointment existingAppointment = createValidAppointment();
            Appointment updatedAppointment = createUpdatedAppointment();

            when(appointmentRepository.findById(any(AppointmentId.class)))
                .thenReturn(Optional.of(existingAppointment));
            when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(updatedAppointment);

            // When
            Appointment result = appointmentService.updateAppointment(updatedAppointment);

            // Then
            assertThat(result).isNotNull();
            verify(appointmentRepository).save(updatedAppointment);
        }

        @Test
        @DisplayName("Debe rechazar actualización de cita inexistente")
        void shouldRejectUpdateOfNonExistentAppointment() {
            // Given
            Appointment appointment = createValidAppointment();
            when(appointmentRepository.findById(any(AppointmentId.class)))
                .thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> appointmentService.updateAppointment(appointment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Appointment to update does not exist");
        }
    }

    @Nested
    @DisplayName("Cancelación de Citas")
    class AppointmentCancellationTests {

        @Test
        @DisplayName("Debe cancelar cita correctamente")
        void shouldCancelAppointmentSuccessfully() {
            // Given
            Appointment existingAppointment = createValidAppointment();
            when(appointmentRepository.findById(any(AppointmentId.class)))
                .thenReturn(Optional.of(existingAppointment));
            when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(existingAppointment);

            // When
            Appointment result = appointmentService.cancelAppointment(AppointmentId.of(existingAppointment.getId()));

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(AppointmentStatus.CANCELADA);
            verify(appointmentRepository).save(any(Appointment.class));
        }

        @Test
        @DisplayName("Debe rechazar cancelación de cita inexistente")
        void shouldRejectCancellationOfNonExistentAppointment() {
            // Given
            AppointmentId nonExistentId = AppointmentId.of("non-existent-id");
            when(appointmentRepository.findById(any(AppointmentId.class)))
                .thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> appointmentService.cancelAppointment(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Appointment to cancel does not exist");
        }
    }

    @Nested
    @DisplayName("Confirmación de Citas")
    class AppointmentConfirmationTests {

        @Test
        @DisplayName("Debe confirmar cita correctamente")
        void shouldConfirmAppointmentSuccessfully() {
            // Given
            Appointment existingAppointment = createValidAppointment();
            when(appointmentRepository.findById(any(AppointmentId.class)))
                .thenReturn(Optional.of(existingAppointment));
            when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(existingAppointment);

            // When
            Appointment result = appointmentService.confirmAppointment(AppointmentId.of(existingAppointment.getId()));

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(AppointmentStatus.CONFIRMADA);
            verify(appointmentRepository).save(any(Appointment.class));
        }

        @Test
        @DisplayName("Debe rechazar confirmación de cita inexistente")
        void shouldRejectConfirmationOfNonExistentAppointment() {
            // Given
            AppointmentId nonExistentId = AppointmentId.of("non-existent-id");
            when(appointmentRepository.findById(any(AppointmentId.class)))
                .thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> appointmentService.confirmAppointment(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Appointment to confirm does not exist");
        }
    }

    @Nested
    @DisplayName("Búsqueda de Citas")
    class AppointmentSearchTests {

        @Test
        @DisplayName("Debe encontrar cita por ID correctamente")
        void shouldFindAppointmentByIdSuccessfully() {
            // Given
            Appointment appointment = createValidAppointment();
            when(appointmentRepository.findById(any(AppointmentId.class)))
                .thenReturn(Optional.of(appointment));

            // When
            Optional<Appointment> result = appointmentService.findAppointmentById(AppointmentId.of(appointment.getId()));

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(appointment.getId());
            verify(appointmentRepository).findById(AppointmentId.of(appointment.getId()));
        }

        @Test
        @DisplayName("Debe retornar vacío cuando cita no existe")
        void shouldReturnEmptyWhenAppointmentNotFound() {
            // Given
            when(appointmentRepository.findById(any(AppointmentId.class)))
                .thenReturn(Optional.empty());

            // When
            Optional<Appointment> result = appointmentService.findAppointmentById(AppointmentId.of("non-existent"));

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Debe encontrar citas por paciente correctamente")
        void shouldFindAppointmentsByPatientSuccessfully() {
            // Given
            PatientCedula patientCedula = PatientCedula.of("12345678");
            List<Appointment> appointments = List.of(createValidAppointment());
            when(appointmentRepository.findByPatientCedula(any(PatientCedula.class)))
                .thenReturn(appointments);

            // When
            List<Appointment> result = appointmentService.findAppointmentsByPatient(patientCedula);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPatientCedula()).isEqualTo(patientCedula);
            verify(appointmentRepository).findByPatientCedula(patientCedula);
        }

        @Test
        @DisplayName("Debe encontrar citas por doctor correctamente")
        void shouldFindAppointmentsByDoctorSuccessfully() {
            // Given
            DoctorCedula doctorCedula = DoctorCedula.of("87654321");
            List<Appointment> appointments = List.of(createValidAppointment());
            when(appointmentRepository.findByDoctorCedula(any(DoctorCedula.class)))
                .thenReturn(appointments);

            // When
            List<Appointment> result = appointmentService.findAppointmentsByDoctor(doctorCedula);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getDoctorCedula()).isEqualTo(doctorCedula);
            verify(appointmentRepository).findByDoctorCedula(doctorCedula);
        }

        @Test
        @DisplayName("Debe encontrar todas las citas correctamente")
        void shouldFindAllAppointmentsSuccessfully() {
            // Given
            List<Appointment> appointments = List.of(createValidAppointment(), createValidAppointment());
            when(appointmentRepository.findAll()).thenReturn(appointments);

            // When
            List<Appointment> result = appointmentService.findAllAppointments();

            // Then
            assertThat(result).hasSize(2);
            verify(appointmentRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Eliminación de Citas")
    class AppointmentDeletionTests {

        @Test
        @DisplayName("Debe eliminar cita correctamente")
        void shouldDeleteAppointmentSuccessfully() {
            // Given
            Appointment appointment = createValidAppointment();
            when(appointmentRepository.findById(any(AppointmentId.class)))
                .thenReturn(Optional.of(appointment));
            doNothing().when(appointmentRepository).deleteById(any(AppointmentId.class));

            // When
            appointmentService.deleteAppointmentById(AppointmentId.of(appointment.getId()));

            // Then
            verify(appointmentRepository).deleteById(AppointmentId.of(appointment.getId()));
        }

        @Test
        @DisplayName("Debe rechazar eliminación de cita inexistente")
        void shouldRejectDeletionOfNonExistentAppointment() {
            // Given
            when(appointmentRepository.findById(any(AppointmentId.class)))
                .thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> appointmentService.deleteAppointmentById(AppointmentId.of("non-existent")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Appointment to delete does not exist");
        }
    }

    @Nested
    @DisplayName("Validaciones de Disponibilidad")
    class AvailabilityValidationTests {

        @Test
        @DisplayName("Debe detectar conflicto de horario correctamente")
        void shouldDetectScheduleConflictCorrectly() {
            // Given
            Appointment existingAppointment = createValidAppointment();
            Appointment conflictingAppointment = createConflictingAppointment();

            when(appointmentRepository.findByDoctorCedula(any(DoctorCedula.class)))
                .thenReturn(List.of(existingAppointment));

            // When & Then
            assertThatThrownBy(() -> appointmentService.scheduleAppointment(conflictingAppointment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Doctor is not available at the requested time");
        }

        @Test
        @DisplayName("Debe permitir cita cuando no hay conflictos")
        void shouldAllowAppointmentWhenNoConflicts() {
            // Given
            Appointment appointment = createValidAppointment();
            when(appointmentRepository.findByDoctorCedula(any(DoctorCedula.class)))
                .thenReturn(List.of()); // No conflicts
            when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(appointment);

            // When
            Appointment result = appointmentService.scheduleAppointment(appointment);

            // Then
            assertThat(result).isNotNull();
            verify(appointmentRepository).save(appointment);
        }

        @Test
        @DisplayName("Debe considerar solo citas programadas y confirmadas para conflictos")
        void shouldOnlyConsiderScheduledAndConfirmedAppointmentsForConflicts() {
            // Given
            Appointment cancelledAppointment = createCancelledAppointment();
            Appointment appointment = createValidAppointment();

            when(appointmentRepository.findByDoctorCedula(any(DoctorCedula.class)))
                .thenReturn(List.of(cancelledAppointment)); // Only cancelled appointment
            when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(appointment);

            // When
            Appointment result = appointmentService.scheduleAppointment(appointment);

            // Then
            assertThat(result).isNotNull();
            verify(appointmentRepository).save(appointment);
        }
    }

    // Métodos auxiliares para crear objetos de prueba
    private Appointment createValidAppointment() {
        return Appointment.of(
            "test-id",
            PatientCedula.of("12345678"),
            DoctorCedula.of("87654321"),
            AppointmentDateTime.of(LocalDateTime.now().plusDays(1)),
            AppointmentStatus.PROGRAMADA,
            ConsultationReason.of("Consulta general")
        );
    }

    private Appointment createPastAppointment() {
        return Appointment.of(
            "past-id",
            PatientCedula.of("12345678"),
            DoctorCedula.of("87654321"),
            AppointmentDateTime.of(LocalDateTime.now().minusDays(1)), // Past date
            AppointmentStatus.PROGRAMADA,
            ConsultationReason.of("Consulta pasada")
        );
    }

    private Appointment createConflictingAppointment() {
        return Appointment.of(
            "conflict-id",
            PatientCedula.of("87654321"),
            DoctorCedula.of("87654321"),
            AppointmentDateTime.of(LocalDateTime.now().plusDays(1)),
            AppointmentStatus.PROGRAMADA,
            ConsultationReason.of("Consulta conflictiva")
        );
    }

    private Appointment createValidAppointmentForSamePatient() {
        return Appointment.of(
            "test-id-2",
            PatientCedula.of("12345678"), // Same patient
            DoctorCedula.of("87654321"),
            AppointmentDateTime.of(LocalDateTime.now().plusDays(2)), // Different time
            AppointmentStatus.PROGRAMADA,
            ConsultationReason.of("Segunda consulta")
        );
    }

    private Appointment createUpdatedAppointment() {
        return Appointment.of(
            "test-id",
            PatientCedula.of("12345678"),
            DoctorCedula.of("87654321"),
            AppointmentDateTime.of(LocalDateTime.now().plusDays(3)), // Different time
            AppointmentStatus.PROGRAMADA,
            ConsultationReason.of("Consulta actualizada")
        );
    }

    private Appointment createCancelledAppointment() {
        return Appointment.of(
            "cancelled-id",
            PatientCedula.of("12345678"),
            DoctorCedula.of("87654321"),
            AppointmentDateTime.of(LocalDateTime.now().plusDays(1)),
            AppointmentStatus.CANCELADA, // Cancelled status
            ConsultationReason.of("Consulta cancelada")
        );
    }
}