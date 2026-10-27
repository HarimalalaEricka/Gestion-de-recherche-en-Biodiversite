package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.IdentificationReference;
import mg.nexora.biodiversity.model.keys.IdentificationReferenceKey;

public interface IdentificationReferenceRepository extends JpaRepository<IdentificationReference, IdentificationReferenceKey> {
}
