package mg.nexora.biodiversity.service;

import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.util.FilterSpec;

public abstract class SecuredService<T> {

    private final JpaSpecificationExecutor<T> specRepository;
    private final AccessGuard guard;
    private final String cheminVersRecherche;
    private final String idField;

    /**
     * @param cheminVersRecherche chemin de l'entité vers sa recherche ("recherche", "dataset.recherche", ...)
     * @param idField             nom du champ ID de l'entité ("datasetID", "eventID", ...)
     */
    protected SecuredService(JpaSpecificationExecutor<T> specRepository,
                             AccessGuard guard,
                             String cheminVersRecherche,
                             String idField) {
        this.specRepository = specRepository;
        this.guard = guard;
        this.cheminVersRecherche = cheminVersRecherche;
        this.idField = idField;
    }

    protected Specification<T> restreindre(Specification<T> spec) {
        return guard.restreindre(spec, cheminVersRecherche);
    }

    @Transactional(readOnly = true)
    public Page<T> findAll(Map<String, String> params, Pageable pageable) {
        return specRepository.findAll(restreindre(FilterSpec.<T>from(params)), pageable);
    }

    /** Vide si la ligne n'existe pas OU n'est pas accessible : le controller répond 404 dans les deux cas. */
    @Transactional(readOnly = true)
    public Optional<T> findById(Integer id) {
        Specification<T> parId = (root, query, cb) -> cb.equal(root.get(idField), id);
        return specRepository.findOne(restreindre(parId));
    }
}