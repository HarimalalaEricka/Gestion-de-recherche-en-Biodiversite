package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.OrganismInteraction;
import mg.nexora.biodiversity.service.OrganismInteractionService;

@RestController
@RequestMapping("/api/organism-interactions")
public class OrganismInteractionController {
    private final OrganismInteractionService service;

    public OrganismInteractionController(OrganismInteractionService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrganismInteraction> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganismInteraction> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrganismInteraction> create(@RequestBody OrganismInteraction entity) {
        OrganismInteraction saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganismInteraction> update(@PathVariable Integer id, @RequestBody OrganismInteraction entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setOrganismInteractionID(id);
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
