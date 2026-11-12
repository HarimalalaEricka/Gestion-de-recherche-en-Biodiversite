package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Licence;
import mg.nexora.biodiversity.repository.LicenceRepository;

@Service
@Transactional
public class LicenceService {
    private final LicenceRepository repository;

    public LicenceService(LicenceRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Licence> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Licence> findById(Integer id) {
        return repository.findById(id);
    }

    public Licence save(Licence entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public Optional<Licence> partialUpdate(Integer id, Licence entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getLicenceName() != null) existing.setLicenceName(entity.getLicenceName());
                    if (entity.getLicenceCode() != null) existing.setLicenceCode(entity.getLicenceCode());
                    if (entity.getLicenceURI() != null) existing.setLicenceURI(entity.getLicenceURI());
                    if (entity.getDescription() != null) existing.setDescription(entity.getDescription());
                    if (entity.getIsActive() != null) existing.setIsActive(entity.getIsActive());
                    return repository.save(existing);
                });
    }
}
