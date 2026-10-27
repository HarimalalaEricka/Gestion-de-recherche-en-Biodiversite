package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.OrganismInteractionType;
import mg.nexora.biodiversity.service.OrganismInteractionTypeService;

@RestController
@RequestMapping("/api/organism-interaction-types")
public class OrganismInteractionTypeController {
    private final OrganismInteractionTypeService service;

    public OrganismInteractionTypeController(OrganismInteractionTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrganismInteractionType> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganismInteractionType> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrganismInteractionType> create(@RequestBody OrganismInteractionType entity) {
        OrganismInteractionType saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganismInteractionType> update(@PathVariable Integer id, @RequestBody OrganismInteractionType entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setOrganismInteractionTypeID(id);
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
