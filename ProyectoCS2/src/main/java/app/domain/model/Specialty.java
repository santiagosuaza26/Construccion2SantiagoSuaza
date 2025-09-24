package app.domain.model;

public class Specialty {
    private final String specialtyId;
    private final String name;
    private final String description;

    public Specialty(String specialtyId, String name, String description) {
        this.specialtyId = specialtyId;
        this.name = name;
        this.description = description;
        validate();
    }

    public String getSpecialtyId() { return specialtyId; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    private void validate() {
        if (specialtyId == null || specialtyId.isBlank()) {
            throw new IllegalArgumentException("Specialty id required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Specialty name required");
        }
    }
}