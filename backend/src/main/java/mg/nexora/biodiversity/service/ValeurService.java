package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Valeur;
import mg.nexora.biodiversity.repository.ValeurRepository;

@Service
@Transactional
public class ValeurService {
    private final ValeurRepository repository;

    public ValeurService(ValeurRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Valeur> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Valeur> findById(Integer id) {
        return repository.findById(id);
    }

    public Valeur save(Valeur entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    public Optional<Valeur> partialUpdate(Integer id, Valeur entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getValeurTexte() != null) existing.setValeurTexte(entity.getValeurTexte());
                    if (entity.getValeurNumeric() != null) existing.setValeurNumeric(entity.getValeurNumeric());
                    if (entity.getValeurBoolean() != null) existing.setValeurBoolean(entity.getValeurBoolean());
                    if (entity.getValeurDate() != null) existing.setValeurDate(entity.getValeurDate());
                    if (entity.getLigneID() != null) existing.setLigneID(entity.getLigneID());
                    if (entity.getAttributID() != null) existing.setAttributID(entity.getAttributID());
                    return repository.save(existing);
                });
    }
}
