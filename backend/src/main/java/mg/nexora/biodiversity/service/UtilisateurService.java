package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Utilisateur;
import mg.nexora.biodiversity.repository.UtilisateurRepository;
import mg.nexora.biodiversity.util.FilterSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class UtilisateurService {
    private final UtilisateurRepository repository;

    public UtilisateurService(UtilisateurRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Utilisateur> findAll(Map<String, String> params, Pageable pageable) {
        return repository.findAll(FilterSpec.<Utilisateur>from(params), pageable);
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

    public Optional<Utilisateur> partialUpdate(Integer id, Utilisateur entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getNom() != null) existing.setNom(entity.getNom());
                    if (entity.getPrenom() != null) existing.setPrenom(entity.getPrenom());
                    if (entity.getEmail() != null) existing.setEmail(entity.getEmail());
                    if (entity.getTelephone() != null) existing.setTelephone(entity.getTelephone());
                    if (entity.getRole() != null) existing.setRole(entity.getRole());
                    if (entity.getStatut() != null) existing.setStatut(entity.getStatut());
                    return repository.save(existing);
                });
    }

    @Transactional(readOnly = true)
    public Integer findIdByEmail(String email) {
        return repository.findByEmail(email)
                .map(Utilisateur::getUtilisateurID)      // ADAPTER : getter de l'ID
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
    }
}
