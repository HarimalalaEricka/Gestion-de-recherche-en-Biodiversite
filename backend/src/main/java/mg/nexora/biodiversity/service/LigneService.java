package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Ligne;
import mg.nexora.biodiversity.repository.LigneRepository;

@Service
@Transactional
public class LigneService {
    private final LigneRepository repository;

    public LigneService(LigneRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Ligne> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Ligne> findById(Integer id) {
        return repository.findById(id);
    }

    public Ligne save(Ligne entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
