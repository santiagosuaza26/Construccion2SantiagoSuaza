package app.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.domain.model.InventoryItem;
import app.domain.repository.InventoryRepository;

@Repository
public class JpaInventoryRepositoryAdapter implements InventoryRepository {

    private final SpringDataInventoryRepository jpa;

    public JpaInventoryRepositoryAdapter(SpringDataInventoryRepository jpa) {
        this.jpa = jpa;
    }

    @Override public void save(InventoryItem item) { jpa.save(item); }
    @Override public Optional<InventoryItem> findById(String id) { return jpa.findById(id); }
    @Override public List<InventoryItem> findAll() { return jpa.findAll(); }
    @Override public void deleteById(String id) { jpa.deleteById(id); }
}
