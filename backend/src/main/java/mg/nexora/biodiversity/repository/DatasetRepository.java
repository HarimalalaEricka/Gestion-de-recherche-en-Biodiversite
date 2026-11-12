package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import mg.nexora.biodiversity.model.Dataset;

public interface DatasetRepository extends JpaRepository<Dataset, Integer>, JpaSpecificationExecutor<Dataset> {
}
