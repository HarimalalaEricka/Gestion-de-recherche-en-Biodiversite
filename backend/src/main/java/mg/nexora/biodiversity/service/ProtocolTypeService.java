package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.ProtocolType;
import mg.nexora.biodiversity.repository.ProtocolTypeRepository;

@Service
@Transactional
public class ProtocolTypeService {
    private final ProtocolTypeRepository repository;

    public ProtocolTypeService(ProtocolTypeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ProtocolType> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ProtocolType> findById(Integer id) {
        return repository.findById(id);
    }

    public ProtocolType save(ProtocolType entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
