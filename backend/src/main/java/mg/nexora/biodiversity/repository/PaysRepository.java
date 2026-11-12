package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Pays;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaysRepository extends JpaRepository<Pays, Integer>, JpaSpecificationExecutor<Pays> {
}
