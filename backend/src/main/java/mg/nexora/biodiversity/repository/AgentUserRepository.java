package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.AgentUser;
import mg.nexora.biodiversity.model.keys.AgentUserKey;

public interface AgentUserRepository extends JpaRepository<AgentUser, AgentUserKey> {
}
