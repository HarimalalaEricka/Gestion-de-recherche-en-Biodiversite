package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.*;
import mg.nexora.biodiversity.service.*;
import mg.nexora.biodiversity.repository.*;
import mg.nexora.biodiversity.util.FilterSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class OccurrenceService extends SecuredService<Occurrence> {
    private final OccurrenceRepository repository;
    private final AgentRepository agentRepository;
    private final OccurrenceAgentRepository occurrenceAgentRepository;

    public OccurrenceService(OccurrenceRepository repository, AgentRepository agentRepository, OccurrenceAgentRepository occurrenceAgentRepository, AccessGuard guard) {
        super(repository, guard, "event.dataset.recherche", "occurrenceID");
        this.repository = repository;
        this.agentRepository = agentRepository;
        this.occurrenceAgentRepository = occurrenceAgentRepository;
    }

    @Transactional
    public Occurrence save(Occurrence entity, Integer utilisateurId) {

        Agent agent = agentRepository.findByUtilisateurId(utilisateurId)
            .orElseThrow(() -> new RuntimeException(
                "Aucun agent associé à cet utilisateur."
            ));

        Occurrence savedOccurrence = repository.save(entity);

        OccurrenceAgent occurrenceAgent = new OccurrenceAgent();
        occurrenceAgent.setOccurrence(savedOccurrence);
        occurrenceAgent.setAgent(agent);

        occurrenceAgentRepository.save(occurrenceAgent);

        return savedOccurrence;
    }

    @Transactional
    public Occurrence update(Occurrence entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<Occurrence> partialUpdate(Integer id, Occurrence entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getIndividualCount() != null) existing.setIndividualCount(entity.getIndividualCount());
                    if (entity.getGeologicalContext() != null) existing.setGeologicalContext(entity.getGeologicalContext());
                    if (entity.getOrganism() != null) existing.setOrganism(entity.getOrganism());
                    if (entity.getOccurrenceStatus() != null) existing.setOccurrenceStatus(entity.getOccurrenceStatus());
                    if (entity.getBasisOfRecord() != null) existing.setBasisOfRecord(entity.getBasisOfRecord());
                    if (entity.getEvent() != null) existing.setEvent(entity.getEvent());
                    return repository.save(existing);
                });
    }
}
