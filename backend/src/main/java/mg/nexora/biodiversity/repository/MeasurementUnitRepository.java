package mg.nexora.biodiversity.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.MeasurementUnit;

public interface MeasurementUnitRepository extends JpaRepository<MeasurementUnit, Integer> {
    Optional<MeasurementUnit> findByMeasurementUnitIgnoreCase(String measurementUnit);
}
