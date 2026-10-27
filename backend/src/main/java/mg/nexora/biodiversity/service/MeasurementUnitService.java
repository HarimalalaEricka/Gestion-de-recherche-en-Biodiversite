package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.MeasurementUnit;
import mg.nexora.biodiversity.repository.MeasurementUnitRepository;

@Service
@Transactional
public class MeasurementUnitService {
    private final MeasurementUnitRepository repository;

    public MeasurementUnitService(MeasurementUnitRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MeasurementUnit> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MeasurementUnit> findById(Integer id) {
        return repository.findById(id);
    }

    public MeasurementUnit save(MeasurementUnit entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
