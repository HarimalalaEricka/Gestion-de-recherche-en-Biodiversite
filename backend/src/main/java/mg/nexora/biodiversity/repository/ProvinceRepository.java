package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Province;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProvinceRepository extends JpaRepository<Province, Integer>, JpaSpecificationExecutor<Province> {
}
