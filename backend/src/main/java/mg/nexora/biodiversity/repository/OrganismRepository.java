package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Organism;

public interface OrganismRepository extends JpaRepository<Organism, Integer> {
}
