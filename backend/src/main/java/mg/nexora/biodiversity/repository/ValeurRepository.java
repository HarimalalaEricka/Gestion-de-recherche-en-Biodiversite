package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Valeur;

public interface ValeurRepository extends JpaRepository<Valeur, Integer> {
}
