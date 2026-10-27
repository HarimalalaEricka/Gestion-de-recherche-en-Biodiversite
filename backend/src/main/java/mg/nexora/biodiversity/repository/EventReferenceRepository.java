package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.EventReference;
import mg.nexora.biodiversity.model.keys.EventReferenceKey;

public interface EventReferenceRepository extends JpaRepository<EventReference, EventReferenceKey> {
}
