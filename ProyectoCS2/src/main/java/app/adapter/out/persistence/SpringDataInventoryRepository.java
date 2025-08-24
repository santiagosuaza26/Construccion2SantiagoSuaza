package app.adapter.out.persistence;

import app.domain.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataInventoryRepository extends JpaRepository<InventoryItem, String> {
}
