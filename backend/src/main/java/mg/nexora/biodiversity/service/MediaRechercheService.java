package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.MediaRecherche;
import mg.nexora.biodiversity.model.enums.*;
import mg.nexora.biodiversity.repository.MediaRechercheRepository;
import mg.nexora.biodiversity.util.FilterSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Service
@Transactional
public class MediaRechercheService {
    private final MediaRechercheRepository repository;

    public MediaRechercheService(MediaRechercheRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<MediaRecherche> findAll(Map<String, String> params, Pageable pageable) {
        return repository.findAll(FilterSpec.<MediaRecherche>from(params), pageable);
    }

    @Transactional(readOnly = true)
    public Optional<MediaRecherche> findById(Integer id) {
        return repository.findById(id);
    }

    public MediaRecherche save(MediaRecherche entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<MediaRecherche> partialUpdate(Integer id, MediaRecherche entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getAccessURI() != null) existing.setAccessURI(entity.getAccessURI());
                    if (entity.getCaption() != null) existing.setCaption(entity.getCaption());
                    if (entity.getComments() != null) existing.setComments(entity.getComments());
                    if (entity.getUtilisateur() != null) existing.setUtilisateur(entity.getUtilisateur());
                    if (entity.getRecherche() != null) existing.setRecherche(entity.getRecherche());
                    if (entity.getTypeMedia() != null) existing.setTypeMedia(entity.getTypeMedia());
                    return repository.save(existing);
                });
    }
    @Transactional(readOnly = true)
    public Page<MediaRecherche> findMesMedias(Integer rechercheId, Integer utilisateurId, Pageable pageable) {
        return repository.findMesMedias(rechercheId, utilisateurId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MediaRecherche> findAllMesMedias(Integer utilisateurId,
                                                Integer rechercheId,
                                                TypeMedia typeMedia,   
                                                Pageable pageable) {

        Specification<MediaRecherche> spec = (root, query, cb) ->
                cb.equal(root.get("utilisateur").get("utilisateurID"), utilisateurId);

        if (rechercheId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("recherche").get("rechercheID"), rechercheId));
        }
        if (typeMedia != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("typeMedia"), typeMedia));
        }

        return repository.findAll(spec, pageable);
    }
}
