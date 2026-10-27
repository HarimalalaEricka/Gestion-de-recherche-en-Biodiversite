package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
