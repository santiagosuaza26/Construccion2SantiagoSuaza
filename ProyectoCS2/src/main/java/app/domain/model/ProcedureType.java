package app.domain.model;

public class ProcedureType {
    private final String procedureId;
    private final String name;
    private final String description;
    private final long price;

    public ProcedureType(String procedureId, String name, String description, long price) {
        this.procedureId = procedureId;
        this.name = name;
        this.description = description;
        this.price = price;
        validate();
    }

    public String getProcedureId() { return procedureId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public long getPrice() { return price; }

    private void validate() {
        if (procedureId == null || procedureId.isBlank()) throw new IllegalArgumentException("Procedure id required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Procedure name required");
        if (price < 0) throw new IllegalArgumentException("Invalid price");
    }
}