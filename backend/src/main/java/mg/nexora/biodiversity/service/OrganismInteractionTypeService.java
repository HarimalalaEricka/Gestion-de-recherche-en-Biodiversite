package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.OrganismInteractionType;
import mg.nexora.biodiversity.repository.OrganismInteractionTypeRepository;

@Service
@Transactional
public class OrganismInteractionTypeService {
    private final OrganismInteractionTypeRepository repository;

    public OrganismInteractionTypeService(OrganismInteractionTypeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<OrganismInteractionType> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<OrganismInteractionType> findById(Integer id) {
        return repository.findById(id);
    }

    public OrganismInteractionType save(OrganismInteractionType entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
