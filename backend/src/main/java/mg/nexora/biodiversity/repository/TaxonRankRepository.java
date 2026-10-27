package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.TaxonRank;

public interface TaxonRankRepository extends JpaRepository<TaxonRank, Integer> {
}
