package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Province;
import mg.nexora.biodiversity.repository.ProvinceRepository;

@Service
@Transactional
public class ProvinceService {
    private final ProvinceRepository repository;

    public ProvinceService(ProvinceRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Province> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Province> findById(Integer id) {
        return repository.findById(id);
    }

    public Province save(Province entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
