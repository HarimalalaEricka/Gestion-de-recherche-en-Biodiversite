package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.OrganismInteraction;
import mg.nexora.biodiversity.repository.OrganismInteractionRepository;

@Service
@Transactional
public class OrganismInteractionService {
    private final OrganismInteractionRepository repository;

    public OrganismInteractionService(OrganismInteractionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<OrganismInteraction> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<OrganismInteraction> findById(Integer id) {
        return repository.findById(id);
    }

    public OrganismInteraction save(OrganismInteraction entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
