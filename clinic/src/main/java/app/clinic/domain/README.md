# Domain Layer - Clinic Management System

## Architecture Overview

This domain layer implements a **Hexagonal Architecture** (also known as Ports and Adapters) following **SOLID principles** for a comprehensive clinic management system.

## Hexagonal Architecture Implementation

### 1. Domain Model (Core)
- **Entities**: `User`, `Patient`, `Appointment`, `InventoryItem`, `MedicationOrder`, `ProcedureOrder`, `DiagnosticAidOrder`
- **Value Objects**: 50+ immutable value objects covering all business data
- **Aggregates**: Grouped related entities with transactional boundaries
- **Domain Services**: Business logic for complex operations

### 2. Ports (Interfaces)
- **Repository Ports**: `UserRepository`, `PatientRepository`, `MedicalRecordRepository`, `OrderRepository`, `InventoryRepository`, `AppointmentRepository`, `BillingRepository`, `PatientVisitRepository`
- **Service Ports**: External service interfaces (ready for future implementation)

### 3. Domain Services
- **UserDomainService**: User management operations
- **PatientDomainService**: Patient registration and management
- **OrderDomainService**: Medical order processing
- **MedicalRecordDomainService**: Health record management
- **BillingDomainService**: Financial calculations and invoicing
- **AppointmentDomainService**: Appointment scheduling and management
- **InventoryDomainService**: Inventory management
- **PatientVisitDomainService**: Patient visit tracking

## SOLID Principles Implementation

### 1. Single Responsibility Principle (SRP)
- Each class has one reason to change
- Value objects encapsulate single data types
- Domain services handle specific business operations
- Repository interfaces define single data access patterns

### 2. Open/Closed Principle (OCP)
- Domain entities are open for extension but closed for modification
- New business rules can be added without changing existing code
- Value objects are immutable and extensible
- Services can be extended with new operations

### 3. Liskov Substitution Principle (LSP)
- All domain services implement their interfaces correctly
- Subtypes can replace parent types without affecting functionality
- Exception hierarchy follows LSP with proper inheritance

### 4. Interface Segregation Principle (ISP)
- Repository interfaces are focused on specific entity operations
- No client depends on methods it doesn't use
- Small, cohesive interfaces for different concerns

### 5. Dependency Inversion Principle (DIP)
- Domain layer depends only on abstractions (interfaces)
- High-level modules don't depend on low-level modules
- Both depend on abstractions
- Infrastructure layer implements the ports

## Key Features Implemented

### 1. User Management
- Role-based access control (Human Resources, Administrative Staff, Information Support, Nurses, Doctors)
- User authentication with secure password validation
- Profile management with validation

### 2. Patient Management
- Complete patient registration with personal data
- Emergency contact management (max 1 contact as per requirements)
- Insurance policy tracking with expiration validation
- Patient data validation and integrity

### 3. Medical Records
- Unstructured medical record storage (NoSQL approach)
- Patient-record-date composite keys
- Medical history tracking
- Doctor consultation records

### 4. Order Management
- Medication orders with dosage and duration
- Procedure orders with frequency and specialist requirements
- Diagnostic aid orders with quantity specifications
- Order validation rules (no mixing of diagnostic aids with treatments)

### 5. Appointment System
- Doctor-patient appointment scheduling
- Appointment status management
- Doctor availability validation
- Appointment conflict prevention

### 6. Inventory Management
- Medical supplies, medications, and equipment tracking
- Cost management and pricing
- Active/inactive status management
- Type-based categorization

### 7. Billing System
- Complex copayment calculations
- Insurance policy integration
- Annual copayment limit tracking (1,000,000 pesos)
- Invoice generation with detailed breakdowns

### 8. Vital Signs Tracking
- Blood pressure, temperature, pulse, oxygen level monitoring
- Valid range validation for all vital signs
- Nurse recording capabilities

## Business Rules Implemented

### 1. Insurance and Billing Rules
- Active insurance policies generate 50,000 pesos copayment
- Annual copayment limit of 1,000,000 pesos
- Expired or inactive insurance requires full payment
- Detailed billing with insurance breakdowns

### 2. Medical Order Rules
- Diagnostic aids cannot be combined with medications or procedures
- Orders must have unique identifiers
- Items within orders are sequentially numbered
- Specialist assistance requirements for complex procedures

### 3. User Role Permissions
- Human Resources: User management only (no patient data access)
- Administrative Staff: Patient registration and appointments
- Information Support: Inventory and technical support
- Nurses: Vital signs and visit recording
- Doctors: Full medical record access and order creation

### 4. Data Validation Rules
- Age limit of 150 years for all persons
- Phone numbers: 1-10 digits for users, exactly 10 for patients and emergencies
- Email format validation with domain requirements
- Address length limit of 30 characters
- Username uniqueness and format validation

## Value Objects (50+ Implemented)

### Basic Types
- `Cedula`, `Email`, `PhoneNumber`, `Password`, `Username`, `Address`
- `FullName`, `BirthDate`, `Gender`, `Money`

### Medical Domain
- `BloodPressure`, `Temperature`, `Pulse`, `OxygenLevel`
- `Dosage`, `TreatmentDuration`, `Frequency`, `Quantity`
- `Symptoms`, `Diagnosis`, `ConsultationReason`, `Observations`

### Business Domain
- `OrderNumber`, `ItemNumber`, `OrderStatus`, `OrderCreationDate`
- `PolicyNumber`, `PolicyStatus`, `PolicyExpirationDate`
- `AppointmentDateTime`, `AppointmentStatus`
- `CopaymentAmount`, `MaximumCopaymentAmount`, `AccumulatedCopayment`

### Inventory and Records
- `InventoryItemId`, `InventoryItemName`, `InventoryItemType`
- `MedicalSpecialty`, `RequiresSpecialistAssistance`
- `PatientRecordDate`, `PatientRecordKey`, `PatientRecordData`
- `EmergencyContactName`, `Relationship`

## Exception Handling

### Custom Domain Exceptions
- `DomainException` (base exception)
- `UserAlreadyExistsException`
- `PatientAlreadyExistsException`
- `MedicalRecordAlreadyExistsException`
- `InvalidAppointmentTimeException`
- `DoctorNotAvailableException`
- `OrderValidationException`
- `InsurancePolicyExpiredException`
- `CopaymentLimitExceededException`
- `InvalidVitalSignsException`
- And 10+ more specific exceptions

## Architecture Benefits

### 1. Domain-Driven Design
- Rich domain model with business logic
- Ubiquitous language implementation
- Bounded contexts clearly defined

### 2. Testability
- Domain logic is independent of infrastructure
- Easy to unit test business rules
- Mock-free domain testing

### 3. Maintainability
- Clear separation of concerns
- SOLID principles ensure code quality
- Easy to extend and modify

### 4. Flexibility
- Technology-agnostic domain layer
- Easy to change infrastructure implementations
- Adapter pattern for external systems

### 5. Scalability
- Modular design supports growth
- Clear interfaces for new features
- Performance optimization points identified

## Future Extensions

This domain layer provides a solid foundation for:
- Infrastructure layer implementation (database adapters)
- REST API controllers and DTOs
- External service integrations
- Event-driven architecture
- Microservices decomposition
- CQRS pattern implementation

## Conclusion

The domain layer successfully implements a robust, scalable, and maintainable foundation for the clinic management system, following industry best practices and architectural principles. The design ensures business rules are properly encapsulated, validated, and protected from external changes.