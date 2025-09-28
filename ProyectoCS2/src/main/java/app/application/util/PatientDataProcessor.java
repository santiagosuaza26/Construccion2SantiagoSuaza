package app.application.util;

import java.util.List;
import java.util.stream.Collectors;

import app.application.dto.response.PatientResponse;
import app.domain.model.Patient;

/**
 * Utilidad para procesar datos de pacientes
 * Demuestra SRP: única responsabilidad de procesar y transformar datos de pacientes
 */
public class PatientDataProcessor {

    private PatientDataProcessor() {
        // Clase de utilidad - no permitir instanciación
    }

    /**
     * Procesa lista de pacientes para respuesta paginada
     */
    public static List<PatientResponse> processPatientsForResponse(List<Patient> patients,
                                                                    int page,
                                                                    int size) {
        if (patients == null) {
            return List.of();
        }

        return patients.stream()
                .skip((long) page * size)
                .limit(size)
                .map(PatientDataProcessor::createPatientResponse)
                .collect(Collectors.toList());
    }

    /**
     * Procesa paciente para respuesta detallada
     */
    public static PatientResponse processPatientForDetailedResponse(Patient patient) {
        if (patient == null) {
            return null;
        }

        PatientResponse response = createPatientResponse(patient);

        // Agregar información adicional para respuesta detallada
        addDetailedInformation(response, patient);

        return response;
    }

    /**
     * Procesa lista de pacientes para exportación
     */
    public static List<String> processPatientsForExport(List<Patient> patients, ExportFormat format) {
        if (patients == null || patients.isEmpty()) {
            return List.of();
        }

        return switch (format) {
            case CSV -> exportToCSV(patients);
            case JSON -> exportToJSON(patients);
            case XML -> exportToXML(patients);
        };
    }

    /**
     * Valida y sanitiza datos de paciente
     */
    public static Patient sanitizePatientData(Patient patient) {
        if (patient == null) {
            return null;
        }

        // Crear nuevo paciente con datos sanitizados
        return new Patient(
            sanitizeString(patient.getIdCard()),
            sanitizeString(patient.getFullName()),
            patient.getBirthDate(),
            sanitizeGender(patient.getGender()),
            sanitizeAddress(patient.getAddress()),
            sanitizePhone(patient.getPhone()),
            sanitizeEmail(patient.getEmail()),
            patient.getCredentials(),
            patient.getEmergencyContact(),
            patient.getInsurancePolicy()
        );
    }

    // Métodos privados auxiliares

    private static PatientResponse createPatientResponse(Patient patient) {
        PatientResponse response = new PatientResponse();
        response.setIdCard(patient.getIdCard());
        response.setFullName(patient.getFullName());
        response.setEmail(patient.getEmail());
        response.setPhone(patient.getPhone());
        response.setGender(patient.getGender());
        response.setAddress(patient.getAddress());

        if (patient.getBirthDate() != null) {
            response.setBirthDate(patient.getBirthDate().toString());
            response.setAge(calculateAge(patient.getBirthDate()));
        }

        if (patient.getCredentials() != null) {
            response.setUsername(patient.getCredentials().getUsername());
        }

        if (patient.getEmergencyContact() != null) {
            response.setEmergencyContact(createEmergencyContactInfo(patient.getEmergencyContact()));
        }

        if (patient.getInsurancePolicy() != null) {
            response.setInsurancePolicy(createInsurancePolicyInfo(patient.getInsurancePolicy()));
        }

        return response;
    }

    private static void addDetailedInformation(PatientResponse response, Patient patient) {
        // Agregar información adicional para respuesta detallada
        response.setRegistrationDate(java.time.LocalDate.now().toString());

        // Calcular estadísticas adicionales
        if (patient.getInsurancePolicy() != null) {
            long daysUntilExpiration = calculateDaysUntilExpiration(patient.getInsurancePolicy().getEndDate());
            // Agregar esta información al response si es necesario
        }
    }

    private static List<String> exportToCSV(List<Patient> patients) {
        return patients.stream()
                .map(patient -> String.format("%s,%s,%s,%s",
                    patient.getIdCard(),
                    patient.getFullName(),
                    patient.getEmail(),
                    patient.getPhone()))
                .collect(Collectors.toList());
    }

    private static List<String> exportToJSON(List<Patient> patients) {
        return patients.stream()
                .map(patient -> String.format("{\"id\":\"%s\",\"name\":\"%s\"}",
                    patient.getIdCard(),
                    patient.getFullName()))
                .collect(Collectors.toList());
    }

    private static List<String> exportToXML(List<Patient> patients) {
        return patients.stream()
                .map(patient -> String.format("<patient><id>%s</id><name>%s</name></patient>",
                    patient.getIdCard(),
                    patient.getFullName()))
                .collect(Collectors.toList());
    }

    private static String sanitizeString(String input) {
        if (input == null) {
            return null;
        }
        return input.trim().replaceAll("\\s+", " ");
    }

    private static String sanitizeGender(String gender) {
        if (gender == null) {
            return null;
        }
        return gender.toLowerCase().trim();
    }

    private static String sanitizeAddress(String address) {
        if (address == null) {
            return null;
        }
        String sanitized = address.trim();
        return sanitized.length() > 30 ? sanitized.substring(0, 30) : sanitized;
    }

    private static String sanitizePhone(String phone) {
        if (phone == null) {
            return null;
        }
        return phone.replaceAll("[^0-9]", "");
    }

    private static String sanitizeEmail(String email) {
        if (email == null) {
            return null;
        }
        return email.toLowerCase().trim();
    }

    private static int calculateAge(java.time.LocalDate birthDate) {
        return java.time.Period.between(birthDate, java.time.LocalDate.now()).getYears();
    }

    private static long calculateDaysUntilExpiration(java.time.LocalDate endDate) {
        if (endDate == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), endDate);
    }

    private static PatientResponse.EmergencyContactInfo createEmergencyContactInfo(app.domain.model.EmergencyContact contact) {
        if (contact == null) {
            return null;
        }
        return new PatientResponse.EmergencyContactInfo(
            contact.getFirstName() + " " + contact.getLastName(),
            contact.getRelationship(),
            contact.getPhone()
        );
    }

    private static PatientResponse.InsurancePolicyInfo createInsurancePolicyInfo(app.domain.model.InsurancePolicy policy) {
        if (policy == null) {
            return null;
        }
        return new PatientResponse.InsurancePolicyInfo(
            policy.getCompany(),
            policy.getPolicyNumber(),
            policy.isActive(),
            policy.getEndDate() != null ? policy.getEndDate().toString() : null,
            policy.getEndDate() != null ? (int) calculateDaysUntilExpiration(policy.getEndDate()) : 0
        );
    }

    public enum ExportFormat {
        CSV, JSON, XML
    }
}