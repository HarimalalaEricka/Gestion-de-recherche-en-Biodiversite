package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.Ligne;
import mg.nexora.biodiversity.service.LigneService;

@RestController
@RequestMapping("/api/lignes")
public class LigneController {
    private final LigneService service;

    public LigneController(LigneService service) {
        this.service = service;
    }

    @GetMapping
    public List<Ligne> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ligne> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ligne> create(@RequestBody Ligne entity) {
        Ligne saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ligne> update(@PathVariable Integer id, @RequestBody Ligne entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setLigneID(id);
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
