package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Occurrence;
import mg.nexora.biodiversity.repository.OccurrenceRepository;

@Service
@Transactional
public class OccurrenceService {
    private final OccurrenceRepository repository;

    public OccurrenceService(OccurrenceRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Occurrence> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Occurrence> findById(Integer id) {
        return repository.findById(id);
    }

    public Occurrence save(Occurrence entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
