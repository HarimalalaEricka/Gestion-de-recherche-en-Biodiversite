package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.BibliographicResource;
import mg.nexora.biodiversity.service.BibliographicResourceService;

@RestController
@RequestMapping("/api/bibliographic-resources")
public class BibliographicResourceController {
    private final BibliographicResourceService service;

    public BibliographicResourceController(BibliographicResourceService service) {
        this.service = service;
    }

    @GetMapping
    public List<BibliographicResource> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BibliographicResource> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BibliographicResource> create(@RequestBody BibliographicResource entity) {
        BibliographicResource saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BibliographicResource> update(@PathVariable Integer id, @RequestBody BibliographicResource entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setReferenceID(id);
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
