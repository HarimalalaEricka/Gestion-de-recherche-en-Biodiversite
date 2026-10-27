package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.MeasurementUnit;

public interface MeasurementUnitRepository extends JpaRepository<MeasurementUnit, Integer> {
}
