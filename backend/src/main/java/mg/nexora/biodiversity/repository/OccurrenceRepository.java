package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Occurrence;

public interface OccurrenceRepository extends JpaRepository<Occurrence, Integer> {
}
