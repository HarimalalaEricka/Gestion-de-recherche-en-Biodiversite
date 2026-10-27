package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Pays;
import mg.nexora.biodiversity.repository.PaysRepository;

@Service
@Transactional
public class PaysService {
    private final PaysRepository repository;

    public PaysService(PaysRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Pays> findAll() {
        return repository.findAll();
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
}
