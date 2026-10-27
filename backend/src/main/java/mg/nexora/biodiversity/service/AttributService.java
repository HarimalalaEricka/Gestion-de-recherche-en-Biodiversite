package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Attribut;
import mg.nexora.biodiversity.repository.AttributRepository;

@Service
@Transactional
public class AttributService {
    private final AttributRepository repository;

    public AttributService(AttributRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Attribut> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Attribut> findById(Integer id) {
        return repository.findById(id);
    }

    public Attribut save(Attribut entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
