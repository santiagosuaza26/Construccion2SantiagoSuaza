package app.clinic.infrastructure.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SpecialtyServiceImpl {

    // Lista de especialidades médicas comunes
    private static final List<String> MEDICAL_SPECIALTIES = Arrays.asList(
        "CARDIOLOGY", "DERMATOLOGY", "ENDOCRINOLOGY", "GASTROENTEROLOGY",
        "HEMATOLOGY", "INFECTIOUS_DISEASE", "NEPHROLOGY", "NEUROLOGY",
        "ONCOLOGY", "OPHTHALMOLOGY", "ORTHOPEDICS", "PEDIATRICS",
        "PSYCHIATRY", "PULMONOLOGY", "RADIOLOGY", "RHEUMATOLOGY",
        "SURGERY", "UROLOGY", "GENERAL_MEDICINE", "OBSTETRICS_GYNECOLOGY"
    );

    public List<String> getAllSpecialties() {
        return MEDICAL_SPECIALTIES;
    }

    public boolean isValidSpecialty(String specialty) {
        return MEDICAL_SPECIALTIES.contains(specialty.toUpperCase());
    }

    public String getSpecialtyByName(String specialtyName) {
        String upperCase = specialtyName.toUpperCase();
        if (MEDICAL_SPECIALTIES.contains(upperCase)) {
            return upperCase;
        }
        throw new RuntimeException("Especialidad no encontrada: " + specialtyName);
    }
}