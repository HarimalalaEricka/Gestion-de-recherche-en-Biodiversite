package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.OrganismInteraction;

public interface OrganismInteractionRepository extends JpaRepository<OrganismInteraction, Integer> {
}
