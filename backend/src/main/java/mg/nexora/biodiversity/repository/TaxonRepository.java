package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import mg.nexora.biodiversity.model.Taxon;
import java.util.Optional;

public interface TaxonRepository extends JpaRepository<Taxon, Integer>, JpaSpecificationExecutor<Taxon> {
    Optional<Taxon> findByGbifKey(Long gbifKey);
    Optional<Taxon> findFirstByScientificNameIgnoreCase(String scientificName);
}