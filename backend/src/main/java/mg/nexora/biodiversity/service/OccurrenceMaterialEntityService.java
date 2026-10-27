package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.OccurrenceMaterialEntity;
import mg.nexora.biodiversity.repository.OccurrenceMaterialEntityRepository;
import mg.nexora.biodiversity.model.keys.*;

@Service
@Transactional
public class OccurrenceMaterialEntityService {
    private final OccurrenceMaterialEntityRepository repository;

    public OccurrenceMaterialEntityService(OccurrenceMaterialEntityRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<OccurrenceMaterialEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<OccurrenceMaterialEntity> findById(OccurrenceMaterialEntityKey id) {
        return repository.findById(id);
    }

    public OccurrenceMaterialEntity save(OccurrenceMaterialEntity entity) {
        return repository.save(entity);
    }

    public void deleteById(OccurrenceMaterialEntityKey id) {
        repository.deleteById(id);
    }
}
