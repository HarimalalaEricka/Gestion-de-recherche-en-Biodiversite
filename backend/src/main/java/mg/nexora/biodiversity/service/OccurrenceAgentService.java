package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.OccurrenceAgent;
import mg.nexora.biodiversity.repository.OccurrenceAgentRepository;
import mg.nexora.biodiversity.model.keys.*;


@Service
@Transactional
public class OccurrenceAgentService {
    private final OccurrenceAgentRepository repository;

    public OccurrenceAgentService(OccurrenceAgentRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<OccurrenceAgent> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<OccurrenceAgent> findById(OccurrenceAgentKey id) {
        return repository.findById(id);
    }

    public OccurrenceAgent save(OccurrenceAgent entity) {
        return repository.save(entity);
    }

    public void deleteById(OccurrenceAgentKey id) {
        repository.deleteById(id);
    }
}
