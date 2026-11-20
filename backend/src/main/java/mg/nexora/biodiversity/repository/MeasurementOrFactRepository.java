package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import mg.nexora.biodiversity.model.MeasurementOrFact;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MeasurementOrFactRepository extends JpaRepository<MeasurementOrFact, Integer>, JpaSpecificationExecutor<MeasurementOrFact> {
    Page<MeasurementOrFact> findByOccurrence_OccurrenceID(Integer occurrenceID, Pageable pageable);
}
