package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.NucleotideAnalysis;
import mg.nexora.biodiversity.repository.NucleotideAnalysisRepository;

@Service
@Transactional
public class NucleotideAnalysisService {
    private final NucleotideAnalysisRepository repository;

    public NucleotideAnalysisService(NucleotideAnalysisRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<NucleotideAnalysis> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<NucleotideAnalysis> findById(Integer id) {
        return repository.findById(id);
    }

    public NucleotideAnalysis save(NucleotideAnalysis entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
