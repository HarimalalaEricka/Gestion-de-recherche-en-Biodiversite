package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Event;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByDataset_DatasetID(Integer datasetId);
}
