package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Protocol;
import mg.nexora.biodiversity.repository.ProtocolRepository;

@Service
@Transactional
public class ProtocolService {
    private final ProtocolRepository repository;

    public ProtocolService(ProtocolRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Protocol> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Protocol> findById(Integer id) {
        return repository.findById(id);
    }

    public Protocol save(Protocol entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<Protocol> partialUpdate(Integer id, Protocol entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getProtocolName() != null) existing.setProtocolName(entity.getProtocolName());
                    if (entity.getProtocolDescription() != null) existing.setProtocolDescription(entity.getProtocolDescription());
                    if (entity.getProtocolReferences() != null) existing.setProtocolReferences(entity.getProtocolReferences());
                    if (entity.getProtocolRemarks() != null) existing.setProtocolRemarks(entity.getProtocolRemarks());
                    if (entity.getProtocolTypeID() != null) existing.setProtocolTypeID(entity.getProtocolTypeID());
                    return repository.save(existing);
                });
    }
}
