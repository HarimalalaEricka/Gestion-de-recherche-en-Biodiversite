package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.OccurrenceMaterialEntity;
import mg.nexora.biodiversity.model.keys.OccurrenceMaterialEntityKey;

public interface OccurrenceMaterialEntityRepository extends JpaRepository<OccurrenceMaterialEntity, OccurrenceMaterialEntityKey> {
}
