package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.GeologicalContext;

public interface GeologicalContextRepository extends JpaRepository<GeologicalContext, Integer> {
}
