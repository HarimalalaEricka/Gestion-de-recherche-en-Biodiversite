package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.MeasurementUnit;
import mg.nexora.biodiversity.repository.MeasurementUnitRepository;
import mg.nexora.biodiversity.util.FilterSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class MeasurementUnitService {
    private final MeasurementUnitRepository repository;

    public MeasurementUnitService(MeasurementUnitRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<MeasurementUnit> findAll(Map<String, String> params, Pageable pageable) {
        return repository.findAll(FilterSpec.<MeasurementUnit>from(params), pageable);
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
    public Optional<MeasurementUnit> partialUpdate(Integer id, MeasurementUnit entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getMeasurementUnit() != null) existing.setMeasurementUnit(entity.getMeasurementUnit());
                    return repository.save(existing);
                });
    }
}
