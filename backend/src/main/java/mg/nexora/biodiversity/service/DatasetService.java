package mg.nexora.biodiversity.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Dataset;
import mg.nexora.biodiversity.repository.DatasetRepository;

@Service
@Transactional
public class DatasetService extends SecuredService<Dataset> {

    private final DatasetRepository repository;

    public DatasetService(DatasetRepository repository, AccessGuard guard) {
        super(repository, guard, "recherche", "datasetID");
        this.repository = repository;
    }

    public Dataset save(Dataset entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        Dataset dataset = findById(id)                       // <-- findById sécurisé hérité
            .orElseThrow(() -> new RuntimeException("Dataset introuvable"));

        dataset.setDeleted(true);
        repository.save(dataset);
    }

    public Optional<Dataset> partialUpdate(Integer id, Dataset entity) {
        return findById(id)                                  // <-- idem
                .map(existing -> {
                    if (entity.getDatasetName() != null) existing.setDatasetName(entity.getDatasetName());
                    if (entity.getLicence() != null) existing.setLicence(entity.getLicence());
                    if (entity.getRightsHolder() != null) existing.setRightsHolder(entity.getRightsHolder());
                    if (entity.getRecherche() != null) existing.setRecherche(entity.getRecherche());
                    return repository.save(existing);
                });
    }
}