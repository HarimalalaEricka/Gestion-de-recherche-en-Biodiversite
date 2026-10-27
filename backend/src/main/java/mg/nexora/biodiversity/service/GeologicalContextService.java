package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.GeologicalContext;
import mg.nexora.biodiversity.repository.GeologicalContextRepository;

@Service
@Transactional
public class GeologicalContextService {
    private final GeologicalContextRepository repository;

    public GeologicalContextService(GeologicalContextRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<GeologicalContext> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<GeologicalContext> findById(Integer id) {
        return repository.findById(id);
    }

    public GeologicalContext save(GeologicalContext entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
