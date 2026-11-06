package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import mg.nexora.biodiversity.model.Recherche;

public interface RechercheRepository extends JpaRepository<Recherche, Integer>, JpaSpecificationExecutor<Recherche> {
}
