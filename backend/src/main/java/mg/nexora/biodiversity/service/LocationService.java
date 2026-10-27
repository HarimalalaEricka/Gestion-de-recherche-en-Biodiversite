package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Location;
import mg.nexora.biodiversity.repository.LocationRepository;

@Service
@Transactional
public class LocationService {
    private final LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Location> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Location> findById(Integer id) {
        return repository.findById(id);
    }

    public Location save(Location entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
