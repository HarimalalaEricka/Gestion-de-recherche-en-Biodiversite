package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Taxon;

public interface TaxonRepository extends JpaRepository<Taxon, Integer> {
}
