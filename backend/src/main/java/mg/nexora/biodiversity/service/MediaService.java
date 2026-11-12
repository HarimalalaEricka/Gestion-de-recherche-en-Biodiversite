package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Media;
import mg.nexora.biodiversity.repository.MediaRepository;

@Service
@Transactional
public class MediaService {
    private final MediaRepository repository;

    public MediaService(MediaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Media> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Media> findById(Integer id) {
        return repository.findById(id);
    }

    public Media save(Media entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<Media> partialUpdate(Integer id, Media entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getAccessURI() != null) existing.setAccessURI(entity.getAccessURI());
                    if (entity.getAssociatedObservationReference() != null) existing.setAssociatedObservationReference(entity.getAssociatedObservationReference());
                    if (entity.getCaption() != null) existing.setCaption(entity.getCaption());
                    if (entity.getCaptureDevice() != null) existing.setCaptureDevice(entity.getCaptureDevice());
                    if (entity.getComments() != null) existing.setComments(entity.getComments());
                    if (entity.getOccurrence() != null) existing.setOccurrence(entity.getOccurrence());
                    return repository.save(existing);
                });
    }
}
