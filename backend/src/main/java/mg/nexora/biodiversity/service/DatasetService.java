package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Dataset;
import mg.nexora.biodiversity.repository.DatasetRepository;

@Service
@Transactional
public class DatasetService {
    private final DatasetRepository repository;

    public DatasetService(DatasetRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Dataset> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Dataset> findById(Integer id) {
        return repository.findById(id);
    }

    public Dataset save(Dataset entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
