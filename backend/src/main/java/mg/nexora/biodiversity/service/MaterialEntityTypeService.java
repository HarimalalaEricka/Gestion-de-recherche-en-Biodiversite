package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.MaterialEntityType;
import mg.nexora.biodiversity.repository.MaterialEntityTypeRepository;

@Service
@Transactional
public class MaterialEntityTypeService {
    private final MaterialEntityTypeRepository repository;

    public MaterialEntityTypeService(MaterialEntityTypeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MaterialEntityType> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MaterialEntityType> findById(Integer id) {
        return repository.findById(id);
    }

    public MaterialEntityType save(MaterialEntityType entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
