package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Pays;
import mg.nexora.biodiversity.repository.PaysRepository;
import mg.nexora.biodiversity.util.FilterSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class PaysService {
    private final PaysRepository repository;

    public PaysService(PaysRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Pays> findAll(Map<String, String> params, Pageable pageable) {
        return repository.findAll(FilterSpec.<Pays>from(params), pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Pays> findById(Integer id) {
        return repository.findById(id);
    }

    public Pays save(Pays entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<Pays> partialUpdate(Integer id, Pays entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getPays() != null) existing.setPays(entity.getPays());
                    return repository.save(existing);
                });
    }
}
