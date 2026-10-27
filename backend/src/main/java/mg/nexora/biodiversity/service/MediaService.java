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
}
