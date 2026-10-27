package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.OccurrenceAgent;
import mg.nexora.biodiversity.model.keys.OccurrenceAgentKey;

public interface OccurrenceAgentRepository extends JpaRepository<OccurrenceAgent, OccurrenceAgentKey> {
}
