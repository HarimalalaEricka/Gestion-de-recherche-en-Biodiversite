package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Recherche;

public interface RechercheRepository extends JpaRepository<Recherche, Integer> {
}
