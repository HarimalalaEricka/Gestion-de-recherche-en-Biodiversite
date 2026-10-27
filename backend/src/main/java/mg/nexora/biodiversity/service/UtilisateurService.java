package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Utilisateur;
import mg.nexora.biodiversity.repository.UtilisateurRepository;

@Service
@Transactional
public class UtilisateurService {
    private final UtilisateurRepository repository;

    public UtilisateurService(UtilisateurRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Utilisateur> findById(Integer id) {
        return repository.findById(id);
    }

    public Utilisateur save(Utilisateur entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
