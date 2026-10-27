package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.TaxonRank;
import mg.nexora.biodiversity.repository.TaxonRankRepository;

@Service
@Transactional
public class TaxonRankService {
    private final TaxonRankRepository repository;

    public TaxonRankService(TaxonRankRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TaxonRank> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TaxonRank> findById(Integer id) {
        return repository.findById(id);
    }

    public TaxonRank save(TaxonRank entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
