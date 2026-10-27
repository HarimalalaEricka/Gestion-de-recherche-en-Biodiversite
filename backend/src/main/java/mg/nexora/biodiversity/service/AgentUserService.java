package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.AgentUser;
import mg.nexora.biodiversity.repository.AgentUserRepository;
import mg.nexora.biodiversity.model.keys.*;


@Service
@Transactional
public class AgentUserService {
    private final AgentUserRepository repository;

    public AgentUserService(AgentUserRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<AgentUser> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<AgentUser> findById(AgentUserKey id) {
        return repository.findById(id);
    }

    public AgentUser save(AgentUser entity) {
        return repository.save(entity);
    }

    public void deleteById(AgentUserKey id) {
        repository.deleteById(id);
    }
}
