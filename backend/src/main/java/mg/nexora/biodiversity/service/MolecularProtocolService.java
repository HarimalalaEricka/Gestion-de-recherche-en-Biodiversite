package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.MolecularProtocol;
import mg.nexora.biodiversity.repository.MolecularProtocolRepository;

@Service
@Transactional
public class MolecularProtocolService {
    private final MolecularProtocolRepository repository;

    public MolecularProtocolService(MolecularProtocolRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MolecularProtocol> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MolecularProtocol> findById(Integer id) {
        return repository.findById(id);
    }

    public MolecularProtocol save(MolecularProtocol entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
