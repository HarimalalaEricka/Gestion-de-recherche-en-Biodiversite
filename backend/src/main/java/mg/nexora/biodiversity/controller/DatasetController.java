package mg.nexora.biodiversity.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.Dataset;
import mg.nexora.biodiversity.service.DatasetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/datasets")
public class DatasetController {
    private final DatasetService service;

    public DatasetController(DatasetService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Dataset> findAll(@RequestParam Map<String, String> params, Pageable pageable) {
        return service.findAll(params, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dataset> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Dataset> create(@RequestBody Dataset entity) {
        Dataset saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dataset> update(@PathVariable Integer id, @RequestBody Dataset entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setDatasetID(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Dataset> partialUpdate(@PathVariable Integer id, @RequestBody Dataset entity) {
        return service.partialUpdate(id, entity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
