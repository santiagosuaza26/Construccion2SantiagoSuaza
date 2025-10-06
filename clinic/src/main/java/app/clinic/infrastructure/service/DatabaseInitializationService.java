package app.clinic.infrastructure.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import app.clinic.infrastructure.entity.UserEntity;
import app.clinic.infrastructure.repository.UserJpaRepository;

/**
 * Service for initializing the database with sample data.
 * Implements CommandLineRunner to execute on application startup.
 */
@Service
public class DatabaseInitializationService implements CommandLineRunner {

    private final UserJpaRepository userJpaRepository;

    public DatabaseInitializationService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeUsers();
        initializeInventory();
    }

    private void initializeUsers() {
        if (userJpaRepository.count() == 0) {
            // Create sample users for testing
            UserEntity admin = new UserEntity(
                    "12345678",
                    "admin",
                    "Admin123!@#",
                    "Administrador",
                    "Sistema",
                    java.time.LocalDate.of(1980, 1, 1),
                    "Dirección Admin",
                    "1234567890",
                    "admin@clinic.com",
                    UserEntity.UserRole.HUMAN_RESOURCES,
                    true
            );

            UserEntity doctor = new UserEntity(
                    "87654321",
                    "doctor",
                    "Doctor123!@#",
                    "Carlos",
                    "Médico",
                    java.time.LocalDate.of(1975, 5, 15),
                    "Dirección Doctor",
                    "0987654321",
                    "doctor@clinic.com",
                    UserEntity.UserRole.DOCTOR,
                    true
            );

            userJpaRepository.save(admin);
            userJpaRepository.save(doctor);
        }
    }

    private void initializeInventory() {
        // This would initialize inventory items if we had an InventoryJpaRepository
        // For now, this is a placeholder for future implementation
    }
}