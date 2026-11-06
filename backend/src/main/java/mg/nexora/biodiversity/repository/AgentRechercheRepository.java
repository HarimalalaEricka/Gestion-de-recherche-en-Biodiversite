package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import mg.nexora.biodiversity.model.AgentRecherche;
import mg.nexora.biodiversity.model.keys.AgentRechercheKey;
import java.util.List;

public interface AgentRechercheRepository extends JpaRepository<AgentRecherche, AgentRechercheKey>, JpaSpecificationExecutor<AgentRecherche> {
    
}
