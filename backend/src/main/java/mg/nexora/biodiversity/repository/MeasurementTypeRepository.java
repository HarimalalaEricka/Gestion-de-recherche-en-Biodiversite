package mg.nexora.biodiversity.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.MeasurementType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MeasurementTypeRepository extends JpaRepository<MeasurementType, Integer>, JpaSpecificationExecutor<MeasurementType> {
    Optional<MeasurementType> findByMeasurementTypeIgnoreCase(String measurementType);
}
