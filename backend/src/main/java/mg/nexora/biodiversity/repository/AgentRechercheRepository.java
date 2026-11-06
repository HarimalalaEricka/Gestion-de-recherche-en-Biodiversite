package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import mg.nexora.biodiversity.model.AgentRecherche;
import mg.nexora.biodiversity.model.keys.AgentRechercheKey;
import java.util.List;

public interface AgentRechercheRepository extends JpaRepository<AgentRecherche, AgentRechercheKey>, JpaSpecificationExecutor<AgentRecherche> {
    @Query("""
        SELECT COUNT(ar) > 0 FROM AgentRecherche ar
        WHERE ar.recherche.rechercheID = :rechercheId
        AND ar.agent.utilisateurId = :utilisateurId
        """)
    boolean estParticipant(@Param("rechercheId") Integer rechercheId,
                        @Param("utilisateurId") Integer utilisateurId);
}
