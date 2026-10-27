package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.MeasurementType;

public interface MeasurementTypeRepository extends JpaRepository<MeasurementType, Integer> {
}
