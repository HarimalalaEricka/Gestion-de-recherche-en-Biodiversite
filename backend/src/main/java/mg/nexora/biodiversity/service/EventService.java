package mg.nexora.biodiversity.service;

import java.util.Map;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Event;
import mg.nexora.biodiversity.repository.EventRepository;
import mg.nexora.biodiversity.util.FilterSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class EventService {
    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Event> findAll(Map<String, String> params, Pageable pageable) {
        return repository.findAll(FilterSpec.<Event>from(params), pageable);
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
                    if (entity.getProtocol() != null) existing.setProtocol(entity.getProtocol());
                    if (entity.getLocation() != null) existing.setLocation(entity.getLocation());
                    if (entity.getDataset() != null) existing.setDataset(entity.getDataset());
                    return repository.save(existing);
                });
    }
}
