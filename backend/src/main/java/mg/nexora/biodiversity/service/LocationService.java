package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Location;
import mg.nexora.biodiversity.repository.LocationRepository;
import mg.nexora.biodiversity.util.FilterSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class LocationService {
    private final LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Location> findAll(Map<String, String> params, Pageable pageable) {
        return repository.findAll(FilterSpec.<Location>from(params), pageable);
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
    public Optional<Location> partialUpdate(Integer id, Location entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getMunicipality() != null) existing.setMunicipality(entity.getMunicipality());
                    if (entity.getLocality() != null) existing.setLocality(entity.getLocality());
                    if (entity.getDecimalLatitude() != null) existing.setDecimalLatitude(entity.getDecimalLatitude());
                    if (entity.getDecimalLongitude() != null) existing.setDecimalLongitude(entity.getDecimalLongitude());
                    if (entity.getGeodeticDatum() != null) existing.setGeodeticDatum(entity.getGeodeticDatum());
                    if (entity.getProvince() != null) existing.setProvince(entity.getProvince());
                    return repository.save(existing);
                });
    }
}
