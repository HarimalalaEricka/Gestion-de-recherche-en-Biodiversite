package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.AgentRecherche;
import mg.nexora.biodiversity.model.keys.AgentRechercheKey;

public interface AgentRechercheRepository extends JpaRepository<AgentRecherche, AgentRechercheKey> {
}
