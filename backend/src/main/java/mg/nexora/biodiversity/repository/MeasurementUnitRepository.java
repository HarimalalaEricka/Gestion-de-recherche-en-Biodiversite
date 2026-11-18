package mg.nexora.biodiversity.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.MeasurementUnit;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MeasurementUnitRepository extends JpaRepository<MeasurementUnit, Integer>, JpaSpecificationExecutor<MeasurementUnit> {
    Optional<MeasurementUnit> findByMeasurementUnitIgnoreCase(String measurementUnit);
}
