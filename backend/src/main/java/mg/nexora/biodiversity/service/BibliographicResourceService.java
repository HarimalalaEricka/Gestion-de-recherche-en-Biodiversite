package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.BibliographicResource;
import mg.nexora.biodiversity.repository.BibliographicResourceRepository;

@Service
@Transactional
public class BibliographicResourceService {
    private final BibliographicResourceRepository repository;

    public BibliographicResourceService(BibliographicResourceRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<BibliographicResource> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<BibliographicResource> findById(Integer id) {
        return repository.findById(id);
    }

    public BibliographicResource save(BibliographicResource entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
