package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
