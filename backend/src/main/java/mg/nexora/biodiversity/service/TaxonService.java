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
    public Optional<Taxon> partialUpdate(Integer id, Taxon entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getScientificName() != null) existing.setScientificName(entity.getScientificName());
                    if (entity.getKingdom() != null) existing.setKingdom(entity.getKingdom());
                    if (entity.getPhylum() != null) existing.setPhylum(entity.getPhylum());
                    if (entity.getTaxonomicClass() != null) existing.setTaxonomicClass(entity.getTaxonomicClass());
                    if (entity.getOrder() != null) existing.setOrder(entity.getOrder());
                    if (entity.getFamily() != null) existing.setFamily(entity.getFamily());
                    if (entity.getGenus() != null) existing.setGenus(entity.getGenus());
                    if (entity.getSpecificEpithet() != null) existing.setSpecificEpithet(entity.getSpecificEpithet());
                    if (entity.getTaxonRank() != null) existing.setTaxonRank(entity.getTaxonRank());
                    return repository.save(existing);
                });
    }
}
