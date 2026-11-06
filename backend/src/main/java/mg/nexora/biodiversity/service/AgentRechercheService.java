package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.AgentRecherche;
import mg.nexora.biodiversity.repository.AgentRechercheRepository;
import mg.nexora.biodiversity.model.keys.*;
import mg.nexora.biodiversity.util.FilterSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class AgentRechercheService {
    private final AgentRechercheRepository repository;

    public AgentRechercheService(AgentRechercheRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<AgentRecherche> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<AgentRecherche> findById(AgentRechercheKey id) {
        return repository.findById(id);
    }

    public AgentRecherche save(AgentRecherche entity) {
        return repository.save(entity);
    }

    public void deleteById(AgentRechercheKey id) {
        repository.deleteById(id);
    }
}
