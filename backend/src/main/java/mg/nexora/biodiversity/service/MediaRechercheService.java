package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.MediaRecherche;
import mg.nexora.biodiversity.repository.MediaRechercheRepository;

@Service
@Transactional
public class MediaRechercheService {
    private final MediaRechercheRepository repository;

    public MediaRechercheService(MediaRechercheRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MediaRecherche> findAll() {
        return repository.findAll();
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
                    if (entity.getUtilisateurID() != null) existing.setUtilisateurID(entity.getUtilisateurID());
                    if (entity.getRechercheID() != null) existing.setRechercheID(entity.getRechercheID());
                    if (entity.getTypeMedia() != null) existing.setTypeMedia(entity.getTypeMedia());
                    return repository.save(existing);
                });
    }
}
