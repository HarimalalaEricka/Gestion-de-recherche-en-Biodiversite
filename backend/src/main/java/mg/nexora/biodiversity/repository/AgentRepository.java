package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Agent;

public interface AgentRepository extends JpaRepository<Agent, Integer> {
}
