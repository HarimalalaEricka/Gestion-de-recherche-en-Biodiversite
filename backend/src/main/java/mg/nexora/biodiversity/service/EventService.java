package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Event;
import mg.nexora.biodiversity.repository.EventRepository;

@Service
@Transactional
public class EventService {
    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Event> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Event> findById(Integer id) {
        return repository.findById(id);
    }

    public Event save(Event entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<Event> partialUpdate(Integer id, Event entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getEventDate() != null) existing.setEventDate(entity.getEventDate());
                    if (entity.getSamplingProtocol() != null) existing.setSamplingProtocol(entity.getSamplingProtocol());
                    if (entity.getProtocolID() != null) existing.setProtocolID(entity.getProtocolID());
                    if (entity.getLocationID() != null) existing.setLocationID(entity.getLocationID());
                    if (entity.getDatasetID() != null) existing.setDatasetID(entity.getDatasetID());
                    return repository.save(existing);
                });
    }
}
