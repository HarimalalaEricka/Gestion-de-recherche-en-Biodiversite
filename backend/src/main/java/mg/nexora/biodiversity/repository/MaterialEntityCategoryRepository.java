package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.MaterialEntityCategory;

public interface MaterialEntityCategoryRepository extends JpaRepository<MaterialEntityCategory, Integer> {
}
