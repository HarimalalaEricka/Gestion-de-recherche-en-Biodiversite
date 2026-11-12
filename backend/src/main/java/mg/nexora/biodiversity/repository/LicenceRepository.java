package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Licence;

public interface LicenceRepository extends JpaRepository<Licence, Integer> {
}
