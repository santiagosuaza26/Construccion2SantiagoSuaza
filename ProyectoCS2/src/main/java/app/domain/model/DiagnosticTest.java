package app.domain.model;

public class DiagnosticTest {
    private final String diagnosticId;
    private final String name;
    private final String description;
    private final long price;

    public DiagnosticTest(String diagnosticId, String name, String description, long price) {
        this.diagnosticId = diagnosticId;
        this.name = name;
        this.description = description;
        this.price = price;
        validate();
    }

    public String getDiagnosticId() { return diagnosticId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public long getPrice() { return price; }

    private void validate() {
        if (diagnosticId == null || diagnosticId.isBlank()) throw new IllegalArgumentException("Diagnostic id required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Diagnostic name required");
        if (price < 0) throw new IllegalArgumentException("Invalid price");
    }
}
