package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.IdentificationReference;
import mg.nexora.biodiversity.repository.IdentificationReferenceRepository;
import mg.nexora.biodiversity.model.keys.*;

@Service
@Transactional
public class IdentificationReferenceService {
    private final IdentificationReferenceRepository repository;

    public IdentificationReferenceService(IdentificationReferenceRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<IdentificationReference> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<IdentificationReference> findById(IdentificationReferenceKey id) {
        return repository.findById(id);
    }

    public IdentificationReference save(IdentificationReference entity) {
        return repository.save(entity);
    }

    public void deleteById(IdentificationReferenceKey id) {
        repository.deleteById(id);
    }
}
