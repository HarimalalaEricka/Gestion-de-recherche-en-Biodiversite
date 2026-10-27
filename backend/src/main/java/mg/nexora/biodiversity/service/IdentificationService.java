package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Identification;
import mg.nexora.biodiversity.repository.IdentificationRepository;

@Service
@Transactional
public class IdentificationService {
    private final IdentificationRepository repository;

    public IdentificationService(IdentificationRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Identification> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Identification> findById(Integer id) {
        return repository.findById(id);
    }

    public Identification save(Identification entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
