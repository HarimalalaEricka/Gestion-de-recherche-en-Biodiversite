package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.MaterialEntity;
import mg.nexora.biodiversity.service.MaterialEntityService;

@RestController
@RequestMapping("/api/material-entitys")
public class MaterialEntityController {
    private final MaterialEntityService service;

    public MaterialEntityController(MaterialEntityService service) {
        this.service = service;
    }

    @GetMapping
    public List<MaterialEntity> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialEntity> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MaterialEntity> create(@RequestBody MaterialEntity entity) {
        MaterialEntity saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialEntity> update(@PathVariable Integer id, @RequestBody MaterialEntity entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setMaterialEntityID(id);
        return ResponseEntity.ok(service.save(entity));
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
