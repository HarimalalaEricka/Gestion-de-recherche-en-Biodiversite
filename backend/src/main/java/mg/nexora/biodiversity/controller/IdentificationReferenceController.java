package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.IdentificationReference;
import mg.nexora.biodiversity.model.keys.IdentificationReferenceKey;
import mg.nexora.biodiversity.service.IdentificationReferenceService;

@RestController
@RequestMapping("/api/identification-references")
public class IdentificationReferenceController {
    private final IdentificationReferenceService service;

    public IdentificationReferenceController(IdentificationReferenceService service) {
        this.service = service;
    }

    @GetMapping
    public List<IdentificationReference> findAll() {
        return service.findAll();
    }

    @GetMapping("/{identificationIDId}/{referenceIDId}")
    public ResponseEntity<IdentificationReference> findById(@PathVariable Integer identificationIDId, @PathVariable Integer referenceIDId) {
        IdentificationReferenceKey id = new IdentificationReferenceKey();
        id.setIdentificationID(identificationIDId);
        id.setReferenceID(referenceIDId);
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<IdentificationReference> create(@RequestBody IdentificationReference entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @DeleteMapping("/{identificationIDId}/{referenceIDId}")
    public ResponseEntity<Void> delete(@PathVariable Integer identificationIDId, @PathVariable Integer referenceIDId) {
        IdentificationReferenceKey id = new IdentificationReferenceKey();
        id.setIdentificationID(identificationIDId);
        id.setReferenceID(referenceIDId);
        if (service.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
