package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Recherche;
import mg.nexora.biodiversity.repository.RechercheRepository;

@Service
@Transactional
public class RechercheService {
    private final RechercheRepository repository;

    public RechercheService(RechercheRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Recherche> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Recherche> findById(Integer id) {
        return repository.findById(id);
    }

    public Recherche save(Recherche entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<Recherche> partialUpdate(Integer id, Recherche entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getSujet() != null) existing.setSujet(entity.getSujet());
                    if (entity.getDescription() != null) existing.setDescription(entity.getDescription());
                    if (entity.getDateDebut() != null) existing.setDateDebut(entity.getDateDebut());
                    if (entity.getDateCreation() != null) existing.setDateCreation(entity.getDateCreation());
                    if (entity.getDateFin() != null) existing.setDateFin(entity.getDateFin());
                    if (entity.getNotePerso() != null) existing.setNotePerso(entity.getNotePerso());
                    if (entity.getConfidentialite() != null) existing.setConfidentialite(entity.getConfidentialite());
                    return repository.save(existing);
                });
    }
}
