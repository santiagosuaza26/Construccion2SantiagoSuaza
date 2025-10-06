package app.clinic.domain.model;

import java.util.Objects;

/**
 * Entity representing a user in the clinic system.
 * Contains user information and role-based access control.
 */
public class User {
    private final UserCedula cedula;
    private final UserUsername username;
    private final UserPassword password;
    private final UserFullName fullName;
    private final UserBirthDate birthDate;
    private final UserAddress address;
    private final UserPhoneNumber phoneNumber;
    private final UserEmail email;
    private final UserRole role;
    private final boolean active;

    private User(UserCedula cedula, UserUsername username, UserPassword password,
                UserFullName fullName, UserBirthDate birthDate, UserAddress address,
                UserPhoneNumber phoneNumber, UserEmail email, UserRole role, boolean active) {
        this.cedula = cedula;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    public static User of(UserCedula cedula, UserUsername username, UserPassword password,
                         UserFullName fullName, UserBirthDate birthDate, UserAddress address,
                         UserPhoneNumber phoneNumber, UserEmail email, UserRole role) {
        return new User(cedula, username, password, fullName, birthDate, address,
                       phoneNumber, email, role, true);
    }

    public static User of(UserCedula cedula, UserUsername username, UserPassword password,
                         UserFullName fullName, UserBirthDate birthDate, UserAddress address,
                         UserPhoneNumber phoneNumber, UserEmail email, UserRole role, boolean active) {
        return new User(cedula, username, password, fullName, birthDate, address,
                       phoneNumber, email, role, active);
    }

    public UserCedula getCedula() {
        return cedula;
    }

    public UserUsername getUsername() {
        return username;
    }

    public UserPassword getPassword() {
        return password;
    }

    public UserFullName getFullName() {
        return fullName;
    }

    public UserBirthDate getBirthDate() {
        return birthDate;
    }

    public UserAddress getAddress() {
        return address;
    }

    public UserPhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public UserEmail getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public int getAge() {
        return birthDate.getAge();
    }

    public boolean canViewPatientInfo() {
        return role != UserRole.HUMAN_RESOURCES;
    }

    public boolean canManageUsers() {
        return role == UserRole.HUMAN_RESOURCES;
    }

    public boolean canRegisterPatients() {
        return role == UserRole.ADMINISTRATIVE_STAFF;
    }

    public boolean canManageInventory() {
        return role == UserRole.INFORMATION_SUPPORT;
    }

    public boolean canRecordVitalSigns() {
        return role == UserRole.NURSE;
    }

    public boolean canCreateMedicalRecords() {
        return role == UserRole.DOCTOR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return active == user.active &&
               Objects.equals(cedula, user.cedula) &&
               Objects.equals(username, user.username) &&
               Objects.equals(password, user.password) &&
               Objects.equals(fullName, user.fullName) &&
               Objects.equals(birthDate, user.birthDate) &&
               Objects.equals(address, user.address) &&
               Objects.equals(phoneNumber, user.phoneNumber) &&
               Objects.equals(email, user.email) &&
               role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cedula, username, password, fullName, birthDate,
                          address, phoneNumber, email, role, active);
    }

    @Override
    public String toString() {
        return String.format("User{cedula=%s, username=%s, role=%s, active=%s}",
                           cedula, username, role, active);
    }
}