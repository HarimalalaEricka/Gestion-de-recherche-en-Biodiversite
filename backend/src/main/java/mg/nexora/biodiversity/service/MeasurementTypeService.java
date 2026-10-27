package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.MeasurementType;
import mg.nexora.biodiversity.repository.MeasurementTypeRepository;

@Service
@Transactional
public class MeasurementTypeService {
    private final MeasurementTypeRepository repository;

    public MeasurementTypeService(MeasurementTypeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MeasurementType> findAll() {
        return repository.findAll();
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
}
