package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Ligne;

public interface LigneRepository extends JpaRepository<Ligne, Integer> {
}
