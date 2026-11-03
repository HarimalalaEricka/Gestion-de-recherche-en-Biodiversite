package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.AgentUser;
import mg.nexora.biodiversity.model.keys.AgentUserKey;
import mg.nexora.biodiversity.model.Utilisateur;
import java.util.Optional;

public interface AgentUserRepository extends JpaRepository<AgentUser, AgentUserKey> {
    Optional<AgentUser> findByUtilisateur(Utilisateur utilisateur);
    Optional<AgentUser> findByUtilisateurAndAgentRoleOrder(Utilisateur utilisateur, Integer agentRoleOrder);
}
