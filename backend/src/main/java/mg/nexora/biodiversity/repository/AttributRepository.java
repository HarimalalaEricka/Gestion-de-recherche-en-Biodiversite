package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Attribut;

public interface AttributRepository extends JpaRepository<Attribut, Integer> {
}
