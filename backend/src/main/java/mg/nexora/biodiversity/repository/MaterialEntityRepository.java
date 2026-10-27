package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.MaterialEntity;

public interface MaterialEntityRepository extends JpaRepository<MaterialEntity, Integer> {
}
