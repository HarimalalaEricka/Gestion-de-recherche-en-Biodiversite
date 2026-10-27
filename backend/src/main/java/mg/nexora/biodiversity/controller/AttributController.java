package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.Attribut;
import mg.nexora.biodiversity.service.AttributService;

@RestController
@RequestMapping("/api/attributs")
public class AttributController {
    private final AttributService service;

    public AttributController(AttributService service) {
        this.service = service;
    }

    @GetMapping
    public List<Attribut> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attribut> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Attribut> create(@RequestBody Attribut entity) {
        Attribut saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attribut> update(@PathVariable Integer id, @RequestBody Attribut entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setAttributID(id);
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
