package app.clinic.application.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.dto.patient.CreatePatientDTO;
import app.clinic.application.dto.patient.PatientDTO;
import app.clinic.application.dto.patient.UpdatePatientDTO;
import app.clinic.application.service.PatientApplicationService;
import jakarta.validation.Valid;

/**
 * REST Controller for patient management operations.
 * Provides HTTP endpoints for registering, reading, updating, and deleting patients.
 * Handles patient information, emergency contacts, and insurance policies.
 * All operations are logged for audit purposes.
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    private final PatientApplicationService patientApplicationService;

    public PatientController(PatientApplicationService patientApplicationService) {
        this.patientApplicationService = patientApplicationService;
    }

    /**
     * Registers a new patient in the system.
     * Only accessible by Administrative Staff role.
     *
     * @param createPatientDTO Patient data including personal info, emergency contacts, and insurance
     * @return ResponseEntity with created patient data and location header
     */
    @PostMapping
    public ResponseEntity<PatientDTO> registerPatient(@Valid @RequestBody CreatePatientDTO createPatientDTO) {
        String cedula = createPatientDTO.getCedula();

        logger.info("Registrando nuevo paciente con cédula: {}", cedula);

        try {
            PatientDTO registeredPatient = patientApplicationService.registerPatient(createPatientDTO);

            logger.info("Paciente registrado exitosamente con cédula: {}", cedula);

            return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/patients/cedula/" + cedula)
                .body(registeredPatient);

        } catch (Exception e) {
            if (e.getMessage().contains("already exists")) {
                logger.warn("Intento de registrar paciente con cédula existente: {} - {}",
                           cedula, e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else if (e.getMessage().contains("invalid")) {
                logger.warn("Datos inválidos al registrar paciente con cédula: {} - {}",
                           cedula, e.getMessage());
                return ResponseEntity.badRequest().build();
            } else {
                logger.error("Error interno al registrar paciente con cédula: {} - {}",
                            cedula, e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    /**
     * Updates an existing patient.
     * Only accessible by Administrative Staff role.
     */
    @PutMapping("/{cedula}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable String cedula,
                                                   @Valid @RequestBody UpdatePatientDTO updatePatientDTO) {
        logger.info("Actualizando paciente con cédula: {}", cedula);

        // Ensure the cedula in path matches the one in request body
        if (!cedula.equals(updatePatientDTO.getCedula())) {
            logger.warn("Inconsistencia en cédula: path={} vs body={} - {}", cedula, updatePatientDTO.getCedula());
            return ResponseEntity.badRequest().build();
        }

        try {
            PatientDTO updatedPatient = patientApplicationService.updatePatient(updatePatientDTO);
            logger.info("Paciente actualizado exitosamente con cédula: {}", cedula);
            return ResponseEntity.ok(updatedPatient);

        } catch (Exception e) {
            if (e.getMessage().contains("not found")) {
                logger.warn("Paciente no encontrado para actualización con cédula: {} - {}",
                           cedula, e.getMessage());
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("invalid")) {
                logger.warn("Datos inválidos al actualizar paciente con cédula: {} - {}",
                           cedula, e.getMessage());
                return ResponseEntity.badRequest().build();
            } else {
                logger.error("Error interno al actualizar paciente con cédula: {} - {}",
                            cedula, e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    /**
     * Finds a patient by their cedula.
     */
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<PatientDTO> findPatientByCedula(@PathVariable String cedula) {
        logger.debug("Buscando paciente por cédula: {}", cedula);

        try {
            return patientApplicationService.findPatientByCedula(cedula)
                    .map(patient -> {
                        logger.debug("Paciente encontrado por cédula: {}", cedula);
                        return ResponseEntity.ok(patient);
                    })
                    .orElseGet(() -> {
                        logger.debug("Paciente no encontrado por cédula: {}", cedula);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Error al buscar paciente por cédula: {} - {}", cedula, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Finds a patient by their username.
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<PatientDTO> findPatientByUsername(@PathVariable String username) {
        logger.debug("Buscando paciente por username: {}", username);

        try {
            return patientApplicationService.findPatientByUsername(username)
                    .map(patient -> {
                        logger.debug("Paciente encontrado por username: {}", username);
                        return ResponseEntity.ok(patient);
                    })
                    .orElseGet(() -> {
                        logger.debug("Paciente no encontrado por username: {}", username);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Error al buscar paciente por username: {} - {}", username, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Finds a patient by their ID.
     */
    @GetMapping("/id/{patientId}")
    public ResponseEntity<PatientDTO> findPatientById(@PathVariable String patientId) {
        logger.debug("Buscando paciente por ID: {}", patientId);

        try {
            return patientApplicationService.findPatientById(patientId)
                    .map(patient -> {
                        logger.debug("Paciente encontrado por ID: {}", patientId);
                        return ResponseEntity.ok(patient);
                    })
                    .orElseGet(() -> {
                        logger.debug("Paciente no encontrado por ID: {}", patientId);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Error al buscar paciente por ID: {} - {}", patientId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Finds all patients.
     */
    @GetMapping
    public ResponseEntity<List<PatientDTO>> findAllPatients() {
        logger.debug("Obteniendo lista de todos los pacientes");

        try {
            List<PatientDTO> patients = patientApplicationService.findAllPatients();
            logger.debug("Se encontraron {} pacientes", patients.size());
            return ResponseEntity.ok(patients);

        } catch (Exception e) {
            logger.error("Error al obtener lista de pacientes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes a patient by their cedula.
     * Only accessible by Administrative Staff role.
     */
    @DeleteMapping("/cedula/{cedula}")
    public ResponseEntity<Void> deletePatientByCedula(@PathVariable String cedula) {
        logger.info("Eliminando paciente por cédula: {}", cedula);

        try {
            patientApplicationService.deletePatientByCedula(cedula);
            logger.info("Paciente eliminado exitosamente por cédula: {}", cedula);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            if (e.getMessage().contains("not found")) {
                logger.warn("Paciente no encontrado para eliminación por cédula: {} - {}",
                           cedula, e.getMessage());
                return ResponseEntity.notFound().build();
            } else {
                logger.error("Error interno al eliminar paciente por cédula: {} - {}",
                            cedula, e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    /**
     * Deletes a patient by their ID.
     * Only accessible by Administrative Staff role.
     */
    @DeleteMapping("/id/{patientId}")
    public ResponseEntity<Void> deletePatientById(@PathVariable String patientId) {
        logger.info("Eliminando paciente por ID: {}", patientId);

        try {
            patientApplicationService.deletePatientById(patientId);
            logger.info("Paciente eliminado exitosamente por ID: {}", patientId);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            if (e.getMessage().contains("not found")) {
                logger.warn("Paciente no encontrado para eliminación por ID: {} - {}",
                           patientId, e.getMessage());
                return ResponseEntity.notFound().build();
            } else {
                logger.error("Error interno al eliminar paciente por ID: {} - {}",
                            patientId, e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    /**
     * Checks if a patient has active insurance.
     */
    @GetMapping("/{cedula}/has-active-insurance")
    public ResponseEntity<Boolean> hasActiveInsurance(@PathVariable String cedula) {
        logger.debug("Verificando seguro activo para paciente con cédula: {}", cedula);

        try {
            boolean hasActiveInsurance = patientApplicationService.hasActiveInsurance(cedula);
            logger.debug("Paciente con cédula {} tiene seguro activo: {}", cedula, hasActiveInsurance);
            return ResponseEntity.ok(hasActiveInsurance);

        } catch (Exception e) {
            if (e.getMessage().contains("not found")) {
                logger.warn("Paciente no encontrado al verificar seguro activo con cédula: {} - {}",
                           cedula, e.getMessage());
                return ResponseEntity.notFound().build();
            } else {
                logger.error("Error al verificar seguro activo para paciente con cédula: {} - {}",
                            cedula, e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    /**
     * Gets the age of a patient.
     */
    @GetMapping("/{cedula}/age")
    public ResponseEntity<Integer> getPatientAge(@PathVariable String cedula) {
        logger.debug("Obteniendo edad para paciente con cédula: {}", cedula);

        try {
            int age = patientApplicationService.getPatientAge(cedula);
            if (age > 0) {
                logger.debug("Paciente con cédula {} tiene {} años", cedula, age);
                return ResponseEntity.ok(age);
            } else {
                logger.debug("Paciente no encontrado o edad inválida para cédula: {}", cedula);
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            logger.error("Error al obtener edad para paciente con cédula: {} - {}",
                        cedula, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}