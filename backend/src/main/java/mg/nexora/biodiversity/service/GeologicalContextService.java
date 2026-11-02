package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.GeologicalContext;
import mg.nexora.biodiversity.repository.GeologicalContextRepository;

@Service
@Transactional
public class GeologicalContextService {
    private final GeologicalContextRepository repository;

    public GeologicalContextService(GeologicalContextRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<GeologicalContext> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<GeologicalContext> findById(Integer id) {
        return repository.findById(id);
    }

    public GeologicalContext save(GeologicalContext entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<GeologicalContext> partialUpdate(Integer id, GeologicalContext entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getGroup() != null) existing.setGroup(entity.getGroup());
                    if (entity.getFormation() != null) existing.setFormation(entity.getFormation());
                    if (entity.getMember() != null) existing.setMember(entity.getMember());
                    if (entity.getBed() != null) existing.setBed(entity.getBed());
                    if (entity.getEarliestPeriodOrLowestSystem() != null) existing.setEarliestPeriodOrLowestSystem(entity.getEarliestPeriodOrLowestSystem());
                    if (entity.getLatestPeriodOrHighestSystem() != null) existing.setLatestPeriodOrHighestSystem(entity.getLatestPeriodOrHighestSystem());
                    if (entity.getEarliestAgeOrLowestStage() != null) existing.setEarliestAgeOrLowestStage(entity.getEarliestAgeOrLowestStage());
                    if (entity.getLatestAgeOrHighestStage() != null) existing.setLatestAgeOrHighestStage(entity.getLatestAgeOrHighestStage());
                    return repository.save(existing);
                });
    }
}
