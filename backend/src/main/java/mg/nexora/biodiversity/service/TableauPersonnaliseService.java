package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.TableauPersonnalise;
import mg.nexora.biodiversity.repository.TableauPersonnaliseRepository;

@Service
@Transactional
public class TableauPersonnaliseService {
    private final TableauPersonnaliseRepository repository;

    public TableauPersonnaliseService(TableauPersonnaliseRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TableauPersonnalise> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TableauPersonnalise> findById(Integer id) {
        return repository.findById(id);
    }

    public TableauPersonnalise save(TableauPersonnalise entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
