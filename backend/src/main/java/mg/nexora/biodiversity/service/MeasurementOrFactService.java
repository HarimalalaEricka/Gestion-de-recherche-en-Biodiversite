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
    public Optional<MeasurementOrFact> partialUpdate(Integer id, MeasurementOrFact entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getMeasurementValue() != null) existing.setMeasurementValue(entity.getMeasurementValue());
                    if (entity.getMeasurementAccuracy() != null) existing.setMeasurementAccuracy(entity.getMeasurementAccuracy());
                    if (entity.getMeasurementMethod() != null) existing.setMeasurementMethod(entity.getMeasurementMethod());
                    if (entity.getMeasurementDeterminedDate() != null) existing.setMeasurementDeterminedDate(entity.getMeasurementDeterminedDate());
                    if (entity.getMeasurementRemarks() != null) existing.setMeasurementRemarks(entity.getMeasurementRemarks());
                    if (entity.getOccurrence() != null) existing.setOccurrence(entity.getOccurrence());
                    if (entity.getMeasurementOrFact1() != null) existing.setMeasurementOrFact1(entity.getMeasurementOrFact1());
                    if (entity.getMeasurementUnit() != null) existing.setMeasurementUnit(entity.getMeasurementUnit());
                    if (entity.getMeasurementType() != null) existing.setMeasurementType(entity.getMeasurementType());
                    return repository.save(existing);
                });
    }
}
