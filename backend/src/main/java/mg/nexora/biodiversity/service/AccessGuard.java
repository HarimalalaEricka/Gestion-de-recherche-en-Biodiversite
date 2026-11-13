package mg.nexora.biodiversity.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import mg.nexora.biodiversity.util.AccessSpecs;

@Component
public class AccessGuard {

    private final CurrentUserService currentUser;

    public AccessGuard(CurrentUserService currentUser) {
        this.currentUser = currentUser;
    }

    /** Ajoute la règle « membre accepté de la recherche », sauf pour un admin. */
    public <T> Specification<T> restreindre(Specification<T> spec, String cheminVersRecherche) {
        if (currentUser.isAdmin()) return spec;
        return spec.and(AccessSpecs.<T>viaRecherche(currentUser.id(), cheminVersRecherche));
    }
}