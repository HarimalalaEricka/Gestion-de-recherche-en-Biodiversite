package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.Identification;
import mg.nexora.biodiversity.service.IdentificationService;

@RestController
@RequestMapping("/api/identifications")
public class IdentificationController {
    private final IdentificationService service;

    public IdentificationController(IdentificationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Identification> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Identification> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Identification> create(@RequestBody Identification entity) {
        Identification saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Identification> update(@PathVariable Integer id, @RequestBody Identification entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setIdentificationID(id);
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
