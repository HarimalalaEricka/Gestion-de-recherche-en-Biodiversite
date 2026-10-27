package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.MaterialEntityCategory;
import mg.nexora.biodiversity.service.MaterialEntityCategoryService;

@RestController
@RequestMapping("/api/material-entity-categorys")
public class MaterialEntityCategoryController {
    private final MaterialEntityCategoryService service;

    public MaterialEntityCategoryController(MaterialEntityCategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<MaterialEntityCategory> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialEntityCategory> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MaterialEntityCategory> create(@RequestBody MaterialEntityCategory entity) {
        MaterialEntityCategory saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialEntityCategory> update(@PathVariable Integer id, @RequestBody MaterialEntityCategory entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setMaterialEntityCategoryID(id);
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
