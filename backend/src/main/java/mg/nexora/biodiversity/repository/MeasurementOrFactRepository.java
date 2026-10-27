package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.MeasurementOrFact;

public interface MeasurementOrFactRepository extends JpaRepository<MeasurementOrFact, Integer> {
}
