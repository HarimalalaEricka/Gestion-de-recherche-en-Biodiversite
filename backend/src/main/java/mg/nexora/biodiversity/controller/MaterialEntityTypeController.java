package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.MaterialEntityType;
import mg.nexora.biodiversity.service.MaterialEntityTypeService;

@RestController
@RequestMapping("/api/material-entity-types")
public class MaterialEntityTypeController {
    private final MaterialEntityTypeService service;

    public MaterialEntityTypeController(MaterialEntityTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<MaterialEntityType> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialEntityType> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MaterialEntityType> create(@RequestBody MaterialEntityType entity) {
        MaterialEntityType saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialEntityType> update(@PathVariable Integer id, @RequestBody MaterialEntityType entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setMaterialEntityTypeID(id);
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
