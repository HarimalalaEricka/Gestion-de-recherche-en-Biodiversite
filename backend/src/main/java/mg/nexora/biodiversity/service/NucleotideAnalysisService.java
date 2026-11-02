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
    public Optional<NucleotideAnalysis> partialUpdate(Integer id, NucleotideAnalysis entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getReadCount() != null) existing.setReadCount(entity.getReadCount());
                    if (entity.getTotalReadCount() != null) existing.setTotalReadCount(entity.getTotalReadCount());
                    if (entity.getIdentificationID() != null) existing.setIdentificationID(entity.getIdentificationID());
                    if (entity.getNucleotideSequenceID() != null) existing.setNucleotideSequenceID(entity.getNucleotideSequenceID());
                    if (entity.getMolecularProtocolID() != null) existing.setMolecularProtocolID(entity.getMolecularProtocolID());
                    if (entity.getMaterialEntityID() != null) existing.setMaterialEntityID(entity.getMaterialEntityID());
                    if (entity.getEventID() != null) existing.setEventID(entity.getEventID());
                    return repository.save(existing);
                });
    }
}
