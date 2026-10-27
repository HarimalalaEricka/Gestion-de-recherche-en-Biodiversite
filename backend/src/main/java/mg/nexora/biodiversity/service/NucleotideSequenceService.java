package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.NucleotideSequence;
import mg.nexora.biodiversity.repository.NucleotideSequenceRepository;

@Service
@Transactional
public class NucleotideSequenceService {
    private final NucleotideSequenceRepository repository;

    public NucleotideSequenceService(NucleotideSequenceRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<NucleotideSequence> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<NucleotideSequence> findById(Integer id) {
        return repository.findById(id);
    }

    public NucleotideSequence save(NucleotideSequence entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
