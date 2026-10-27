package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.OrganismInteractionType;

public interface OrganismInteractionTypeRepository extends JpaRepository<OrganismInteractionType, Integer> {
}
