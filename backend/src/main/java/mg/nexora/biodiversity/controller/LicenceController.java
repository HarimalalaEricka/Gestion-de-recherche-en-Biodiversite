package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.Licence;
import mg.nexora.biodiversity.service.LicenceService;

@RestController
@RequestMapping("/api/licences")
public class LicenceController {
    private final LicenceService service;

    public LicenceController(LicenceService service) {
        this.service = service;
    }

    @GetMapping
    public List<Licence> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Licence> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Licence> create(@RequestBody Licence entity) {
        Licence saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Licence> update(@PathVariable Integer id, @RequestBody Licence entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        entity.setLicenceID(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Licence> partialUpdate(@PathVariable Integer id, @RequestBody Licence entity) {
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
