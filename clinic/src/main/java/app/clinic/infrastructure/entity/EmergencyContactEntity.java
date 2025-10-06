package app.clinic.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity representing emergency contacts table in the database.
 * Maps domain EmergencyContact objects to database records.
 */
@Entity
@Table(name = "emergency_contacts")
public class EmergencyContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_names", nullable = false, length = 50)
    private String firstNames;

    @Column(name = "last_names", nullable = false, length = 50)
    private String lastNames;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship", nullable = false)
    private Relationship relationship;

    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;

    // Default constructor
    public EmergencyContactEntity() {}

    // Constructor with parameters
    public EmergencyContactEntity(String firstNames, String lastNames, Relationship relationship, String phoneNumber) {
        this.firstNames = firstNames;
        this.lastNames = lastNames;
        this.relationship = relationship;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getFullName() {
        return firstNames + " " + lastNames;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Enumeration for relationship types in the database.
     */
    public enum Relationship {
        PADRE, MADRE, HIJO, HIJA, HERMANO, HERMANA,
        ESPOSO, ESPOSA, PAREJA, AMIGO, AMIGA,
        TIO, TIA, ABUEL, ABUELA, OTRO
    }
}