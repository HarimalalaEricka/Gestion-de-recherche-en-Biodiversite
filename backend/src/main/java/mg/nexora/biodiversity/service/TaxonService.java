package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Taxon;
import mg.nexora.biodiversity.repository.TaxonRepository;

@Service
@Transactional
public class TaxonService {
    private final TaxonRepository repository;

    public TaxonService(TaxonRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Taxon> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Taxon> findById(Integer id) {
        return repository.findById(id);
    }

    public Taxon save(Taxon entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
