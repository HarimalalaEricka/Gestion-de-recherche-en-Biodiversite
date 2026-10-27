package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Identification;

public interface IdentificationRepository extends JpaRepository<Identification, Integer> {
}
