package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Notification;
import mg.nexora.biodiversity.repository.NotificationRepository;

@Service
@Transactional
public class NotificationService {
    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Notification> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Notification> findById(Integer id) {
        return repository.findById(id);
    }

    public Notification save(Notification entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<Notification> partialUpdate(Integer id, Notification entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getType() != null) existing.setType(entity.getType());
                    if (entity.getTitre() != null) existing.setTitre(entity.getTitre());
                    if (entity.getLue() != null) existing.setLue(entity.getLue());
                    if (entity.getCreatedAt() != null) existing.setCreatedAt(entity.getCreatedAt());
                    if (entity.getReadAt() != null) existing.setReadAt(entity.getReadAt());
                    if (entity.getMessage() != null) existing.setMessage(entity.getMessage());
                    if (entity.getLien() != null) existing.setLien(entity.getLien());
                    if (entity.getUtilisateurID() != null) existing.setUtilisateurID(entity.getUtilisateurID());
                    return repository.save(existing);
                });
    }
}
