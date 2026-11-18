package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.MeasurementType;
import mg.nexora.biodiversity.repository.MeasurementTypeRepository;
import mg.nexora.biodiversity.util.FilterSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class MeasurementTypeService {
    private final MeasurementTypeRepository repository;

    public MeasurementTypeService(MeasurementTypeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<MeasurementType> findAll(Map<String, String> params, Pageable pageable) {
        return repository.findAll(FilterSpec.<MeasurementType>from(params), pageable);
    }

    @Transactional(readOnly = true)
    public Optional<MeasurementType> findById(Integer id) {
        return repository.findById(id);
    }

    public MeasurementType save(MeasurementType entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<MeasurementType> partialUpdate(Integer id, MeasurementType entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getMeasurementType() != null) existing.setMeasurementType(entity.getMeasurementType());
                    return repository.save(existing);
                });
    }
}
