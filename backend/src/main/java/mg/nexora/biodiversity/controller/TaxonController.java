package mg.nexora.biodiversity.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.Taxon;
import mg.nexora.biodiversity.service.TaxonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/taxons")
public class TaxonController {
    private final TaxonService service;

    public TaxonController(TaxonService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Taxon> findAll(@RequestParam Map<String, String> params, Pageable pageable) {
        return service.findAll(params, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taxon> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Taxon> create(@RequestBody Taxon entity) {
        Taxon saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Taxon> update(@PathVariable Integer id, @RequestBody Taxon entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setTaxonID(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Taxon> partialUpdate(@PathVariable Integer id, @RequestBody Taxon entity) {
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
}
