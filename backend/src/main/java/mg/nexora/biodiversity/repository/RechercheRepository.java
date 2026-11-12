package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import mg.nexora.biodiversity.model.Recherche;
import mg.nexora.biodiversity.model.enums.*;

public interface RechercheRepository extends JpaRepository<Recherche, Integer>, JpaSpecificationExecutor<Recherche> {

    @Query("""
        SELECT r FROM Recherche r
        WHERE (r.confidentialite = :confidentialite OR :confidentialite IS NULL)
          AND LOWER(r.sujet) LIKE LOWER(CONCAT('%', :sujet, '%'))
          AND EXISTS (
            SELECT 1 FROM AgentRecherche ar
            WHERE ar.recherche = r
              AND ar.agent.utilisateurId = :utilisateurId
          )
        """)
    Page<Recherche> findByUtilisateurId(
            @Param("utilisateurId") Integer utilisateurId,
            @Param("confidentialite") Confidentiality confidentialite,
            @Param("sujet") String sujet,
            Pageable pageable);
}
