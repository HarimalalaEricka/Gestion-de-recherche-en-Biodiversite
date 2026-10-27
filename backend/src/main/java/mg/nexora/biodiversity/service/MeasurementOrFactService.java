package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.MeasurementOrFact;
import mg.nexora.biodiversity.repository.MeasurementOrFactRepository;

@Service
@Transactional
public class MeasurementOrFactService {
    private final MeasurementOrFactRepository repository;

    public MeasurementOrFactService(MeasurementOrFactRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MeasurementOrFact> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MeasurementOrFact> findById(Integer id) {
        return repository.findById(id);
    }

    public MeasurementOrFact save(MeasurementOrFact entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
