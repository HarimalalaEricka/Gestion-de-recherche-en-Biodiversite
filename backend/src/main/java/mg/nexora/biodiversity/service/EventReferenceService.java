package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.EventReference;
import mg.nexora.biodiversity.repository.EventReferenceRepository;
import mg.nexora.biodiversity.model.keys.*;


@Service
@Transactional
public class EventReferenceService {
    private final EventReferenceRepository repository;

    public EventReferenceService(EventReferenceRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<EventReference> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<EventReference> findById(EventReferenceKey id) {
        return repository.findById(id);
    }

    public EventReference save(EventReference entity) {
        return repository.save(entity);
    }

    public void deleteById(EventReferenceKey id) {
        repository.deleteById(id);
    }
}
