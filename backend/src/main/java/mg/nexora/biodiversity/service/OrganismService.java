package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Organism;
import mg.nexora.biodiversity.repository.OrganismRepository;

@Service
@Transactional
public class OrganismService {
    private final OrganismRepository repository;

    public OrganismService(OrganismRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Organism> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Organism> findById(Integer id) {
        return repository.findById(id);
    }

    public Organism save(Organism entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<Organism> partialUpdate(Integer id, Organism entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getOrganismName() != null) existing.setOrganismName(entity.getOrganismName());
                    if (entity.getCauseOfDeath() != null) existing.setCauseOfDeath(entity.getCauseOfDeath());
                    if (entity.getOrganismRemarks() != null) existing.setOrganismRemarks(entity.getOrganismRemarks());
                    if (entity.getOrganismScope() != null) existing.setOrganismScope(entity.getOrganismScope());
                    return repository.save(existing);
                });
    }
}
