package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.MaterialEntityCategory;
import mg.nexora.biodiversity.repository.MaterialEntityCategoryRepository;

@Service
@Transactional
public class MaterialEntityCategoryService {
    private final MaterialEntityCategoryRepository repository;

    public MaterialEntityCategoryService(MaterialEntityCategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MaterialEntityCategory> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MaterialEntityCategory> findById(Integer id) {
        return repository.findById(id);
    }

    public MaterialEntityCategory save(MaterialEntityCategory entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
