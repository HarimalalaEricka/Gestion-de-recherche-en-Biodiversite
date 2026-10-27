package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.Valeur;
import mg.nexora.biodiversity.service.ValeurService;

@RestController
@RequestMapping("/api/valeurs")
public class ValeurController {
    private final ValeurService service;

    public ValeurController(ValeurService service) {
        this.service = service;
    }

    @GetMapping
    public List<Valeur> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Valeur> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Valeur> create(@RequestBody Valeur entity) {
        Valeur saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Valeur> update(@PathVariable Integer id, @RequestBody Valeur entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setValeurID(id);
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
