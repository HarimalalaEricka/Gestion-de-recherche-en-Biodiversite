package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.MaterialEntity;
import mg.nexora.biodiversity.repository.MaterialEntityRepository;

@Service
@Transactional
public class MaterialEntityService {
    private final MaterialEntityRepository repository;

    public MaterialEntityService(MaterialEntityRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MaterialEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MaterialEntity> findById(Integer id) {
        return repository.findById(id);
    }

    public MaterialEntity save(MaterialEntity entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
