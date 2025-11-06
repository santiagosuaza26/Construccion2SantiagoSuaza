package app.clinic.domain.model.entities;

import app.clinic.domain.model.valueobject.Address;
import app.clinic.domain.model.valueobject.DateOfBirth;
import app.clinic.domain.model.valueobject.Email;
import app.clinic.domain.model.valueobject.EmergencyContact;
import app.clinic.domain.model.valueobject.Gender;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Phone;

public class Patient {
    private final Id identificationNumber;
    private final String fullName;
    private final DateOfBirth dateOfBirth;
    private final Gender gender;
    private final Address address;
    private final Phone phone;
    private final Email email;
    private final EmergencyContact emergencyContact;
    private final Insurance insurance;
    private double annualCopayTotal;


    public Patient(Id identificationNumber, String fullName, DateOfBirth dateOfBirth, Gender gender, Address address, Phone phone, Email email, EmergencyContact emergencyContact, Insurance insurance) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        this.identificationNumber = identificationNumber;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.emergencyContact = emergencyContact;
        this.insurance = insurance;
        this.annualCopayTotal = 0.0;
    }

    public Id getIdentificationNumber() {
        return identificationNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public Address getAddress() {
        return address;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public double getAnnualCopayTotal() {
        return annualCopayTotal;
    }

    public void addToAnnualCopayTotal(double amount) {
        this.annualCopayTotal += amount;
    }

    public int calculateAge() {
        return (int) java.time.temporal.ChronoUnit.YEARS.between(dateOfBirth.getValue(), java.time.LocalDate.now());
    }

    // Nota: Los métodos relacionados con estado médico ahora se manejan en PatientMedicalState
    // Para mantener compatibilidad temporal, se pueden agregar métodos delegados si es necesario

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return identificationNumber.equals(patient.identificationNumber);
    }

    @Override
    public int hashCode() {
        return identificationNumber.hashCode();
    }

    @Override
    public String toString() {
        return fullName + " (" + identificationNumber + ")";
    }
}