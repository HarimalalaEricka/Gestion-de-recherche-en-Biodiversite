package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.Organism;
import mg.nexora.biodiversity.service.OrganismService;

@RestController
@RequestMapping("/api/organisms")
public class OrganismController {
    private final OrganismService service;

    public OrganismController(OrganismService service) {
        this.service = service;
    }

    @GetMapping
    public List<Organism> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organism> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Organism> create(@RequestBody Organism entity) {
        Organism saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organism> update(@PathVariable Integer id, @RequestBody Organism entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setOrganismID(id);
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
