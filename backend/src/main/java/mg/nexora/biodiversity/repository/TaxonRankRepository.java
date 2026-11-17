package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.TaxonRank;
import java.util.Optional;

public interface TaxonRankRepository extends JpaRepository<TaxonRank, Integer> {
    Optional<TaxonRank> findFirstByTaxonRankIgnoreCase(String taxonRank);
}
