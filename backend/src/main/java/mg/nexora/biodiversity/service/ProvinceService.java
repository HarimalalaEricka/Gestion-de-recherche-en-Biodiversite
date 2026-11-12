package mg.nexora.biodiversity.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mg.nexora.biodiversity.model.Province;
import mg.nexora.biodiversity.repository.ProvinceRepository;
import mg.nexora.biodiversity.util.FilterSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ProvinceService {
    private final ProvinceRepository repository;

    public ProvinceService(ProvinceRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Province> findAll(Map<String, String> params, Pageable pageable) {
        return repository.findAll(FilterSpec.<Province>from(params), pageable);
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
    public Optional<Province> partialUpdate(Integer id, Province entity) {
        return repository.findById(id)
                .map(existing -> {
                    if (entity.getProvince() != null) existing.setProvince(entity.getProvince());
                    if (entity.getPays() != null) existing.setPays(entity.getPays());
                    return repository.save(existing);
                });
    }
}
