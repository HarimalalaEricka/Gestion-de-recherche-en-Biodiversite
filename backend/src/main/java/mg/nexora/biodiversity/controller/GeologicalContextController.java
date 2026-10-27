package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.GeologicalContext;
import mg.nexora.biodiversity.service.GeologicalContextService;

@RestController
@RequestMapping("/api/geological-contexts")
public class GeologicalContextController {
    private final GeologicalContextService service;

    public GeologicalContextController(GeologicalContextService service) {
        this.service = service;
    }

    @GetMapping
    public List<GeologicalContext> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeologicalContext> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GeologicalContext> create(@RequestBody GeologicalContext entity) {
        GeologicalContext saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeologicalContext> update(@PathVariable Integer id, @RequestBody GeologicalContext entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setGeologicalContextID(id);
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
