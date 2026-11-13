package mg.nexora.biodiversity.util;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import mg.nexora.biodiversity.model.AgentRecherche;
import mg.nexora.biodiversity.model.enums.StatutCollaboration;

public class AccessSpecs {

    /**
     * Ne garde que les lignes rattachées à une recherche dont l'utilisateur est membre accepté.
     *
     * @param cheminVersRecherche chemin depuis l'entité jusqu'à sa recherche
     *                            ("recherche" pour Dataset, "dataset.recherche" pour Event,
     *                            "" si l'entité EST la recherche)
     */
    public static <T> Specification<T> viaRecherche(Integer userId, String cheminVersRecherche) {
        return (root, query, cb) -> {
            Path<?> recherche = root;

            if (cheminVersRecherche != null && !cheminVersRecherche.isBlank()) {
                for (String part : cheminVersRecherche.split("\\.")) {
                    recherche = recherche.get(part);
                }
            }

            Subquery<Integer> sq = query.subquery(Integer.class);
            Root<AgentRecherche> ar = sq.from(AgentRecherche.class);

            sq.select(cb.literal(1)).where(
                cb.equal(ar.get("recherche").get("rechercheID"), recherche.get("rechercheID")),
                cb.equal(ar.get("agent").get("utilisateurId"), userId),
                cb.equal(ar.get("statut"), StatutCollaboration.ACCEPTEE)
            );

            return cb.exists(sq);
        };
    }
}