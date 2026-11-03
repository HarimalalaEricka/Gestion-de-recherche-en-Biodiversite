package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.Recherche;
import mg.nexora.biodiversity.service.RechercheService;
import mg.nexora.biodiversity.dto.CreerRechercheRequest;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/recherches")
public class RechercheController {
    private final RechercheService service;

    public RechercheController(RechercheService service) {
        this.service = service;
    }

    @GetMapping
    public List<Recherche> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recherche> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Recherche> create(@RequestBody Recherche entity) {
        Recherche saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recherche> update(@PathVariable Integer id, @RequestBody Recherche entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setRechercheID(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Recherche> partialUpdate(@PathVariable Integer id, @RequestBody Recherche entity) {
        return service.partialUpdate(id, entity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/creer")
    public Recherche creer(@RequestBody CreerRechercheRequest req, Authentication auth) {
        return service.creerAvecAgent(req, auth.getName());
    }
}
