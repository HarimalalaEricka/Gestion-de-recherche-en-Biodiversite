package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Agent;
import mg.nexora.biodiversity.repository.AgentRepository;

@Service
@Transactional
public class AgentService {
    private final AgentRepository repository;

    public AgentService(AgentRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Agent> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Agent> findById(Integer id) {
        return repository.findById(id);
    }

    public Agent save(Agent entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
