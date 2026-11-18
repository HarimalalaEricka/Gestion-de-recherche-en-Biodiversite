package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import mg.nexora.biodiversity.model.MeasurementOrFact;

public interface MeasurementOrFactRepository extends JpaRepository<MeasurementOrFact, Integer> {
    Page<MeasurementOrFact> findByOccurrence_OccurrenceID(Integer occurrenceID, Pageable pageable);
}
