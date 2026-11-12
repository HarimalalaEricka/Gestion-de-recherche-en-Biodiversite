package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import mg.nexora.biodiversity.model.*;
import mg.nexora.biodiversity.model.enums.*;
import mg.nexora.biodiversity.model.keys.AgentRechercheKey;
import java.util.List;
import java.util.Optional;

public interface AgentRechercheRepository extends JpaRepository<AgentRecherche, AgentRechercheKey>, JpaSpecificationExecutor<AgentRecherche> {

    @Query("""
        SELECT COUNT(ar) > 0 FROM AgentRecherche ar
        WHERE ar.recherche.rechercheID = :rechercheId
        AND ar.agent.utilisateurId = :utilisateurId
        """)
    boolean estParticipant(@Param("rechercheId") Integer rechercheId,
                        @Param("utilisateurId") Integer utilisateurId);


    @Query("SELECT ar FROM AgentRecherche ar " +
           "WHERE ar.agent.utilisateurId = :utilisateurId " +
           "AND ar.recherche.rechercheID = :rechercheId")
    Optional<AgentRecherche> trouverParUtilisateurEtRecherche(@Param("utilisateurId") Integer utilisateurId,
                                                              @Param("rechercheId") Integer rechercheId);

    @Query("SELECT COUNT(ar) > 0 FROM AgentRecherche ar " +
           "WHERE ar.agent.utilisateurId = :utilisateurId " +
           "AND ar.recherche.rechercheID = :rechercheId " +
           "AND ar.roleCollaboration = :role AND ar.statut = :statut")
    boolean existeAvecRole(@Param("utilisateurId") Integer utilisateurId,
                           @Param("rechercheId") Integer rechercheId,
                           @Param("role") RoleCollaboration role,
                           @Param("statut") StatutCollaboration statut);
}