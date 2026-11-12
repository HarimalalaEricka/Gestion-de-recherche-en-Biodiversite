package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Dataset;
import mg.nexora.biodiversity.repository.DatasetRepository;
import mg.nexora.biodiversity.util.FilterSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class DatasetService {
    private final DatasetRepository repository;

    public DatasetService(DatasetRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Dataset> findAll(Map<String, String> params, Pageable pageable) {
        return repository.findAll(FilterSpec.<Dataset>from(params), pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Dataset> findById(Integer id) {
        return repository.findById(id);
    }

    public Dataset save(Dataset entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        Dataset dataset = repository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Dataset introuvable")
            );

        dataset.setDeleted(true);
        repository.save(dataset);
    }
    public Optional<Dataset> partialUpdate(Integer id, Dataset entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getDatasetName() != null) existing.setDatasetName(entity.getDatasetName());
                    if (entity.getLicence() != null) existing.setLicence(entity.getLicence());
                    if (entity.getRightsHolder() != null) existing.setRightsHolder(entity.getRightsHolder());
                    if (entity.getRecherche() != null) existing.setRecherche(entity.getRecherche());
                    return repository.save(existing);
                });
    }
}
