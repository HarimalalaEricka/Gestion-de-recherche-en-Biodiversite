package mg.nexora.biodiversity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import mg.nexora.biodiversity.model.MediaRecherche;

public interface MediaRechercheRepository
        extends JpaRepository<MediaRecherche, Integer>, JpaSpecificationExecutor<MediaRecherche> {

    @Query("""
        SELECT m FROM MediaRecherche m
        WHERE m.recherche.rechercheID = :rechercheId
          AND m.utilisateur.utilisateurID = :utilisateurId
        """)
    Page<MediaRecherche> findMesMedias(
            @Param("rechercheId") Integer rechercheId,
            @Param("utilisateurId") Integer utilisateurId,
            Pageable pageable);
}