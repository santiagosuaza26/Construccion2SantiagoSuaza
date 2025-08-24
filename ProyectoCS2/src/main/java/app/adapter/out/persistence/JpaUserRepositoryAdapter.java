package app.adapter.out.persistence;

import app.domain.model.User;
import app.domain.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JpaUserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;

    public JpaUserRepositoryAdapter(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public void save(User user) {
        springDataUserRepository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return springDataUserRepository.findById(id);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return springDataUserRepository.findByUserName(userName);
    }

    @Override
    public List<User> findAll() {
        return springDataUserRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        springDataUserRepository.deleteById(id);
    }
}
