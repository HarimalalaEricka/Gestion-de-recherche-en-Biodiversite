package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Dataset;

public interface DatasetRepository extends JpaRepository<Dataset, Integer> {
}
