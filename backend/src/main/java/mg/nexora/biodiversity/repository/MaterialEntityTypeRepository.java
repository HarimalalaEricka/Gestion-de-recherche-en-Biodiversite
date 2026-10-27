package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.MaterialEntityType;

public interface MaterialEntityTypeRepository extends JpaRepository<MaterialEntityType, Integer> {
}
