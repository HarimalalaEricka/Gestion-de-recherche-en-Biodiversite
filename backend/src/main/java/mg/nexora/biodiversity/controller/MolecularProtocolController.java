package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.MolecularProtocol;
import mg.nexora.biodiversity.service.MolecularProtocolService;

@RestController
@RequestMapping("/api/molecular-protocols")
public class MolecularProtocolController {
    private final MolecularProtocolService service;

    public MolecularProtocolController(MolecularProtocolService service) {
        this.service = service;
    }

    @GetMapping
    public List<MolecularProtocol> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MolecularProtocol> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MolecularProtocol> create(@RequestBody MolecularProtocol entity) {
        MolecularProtocol saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MolecularProtocol> update(@PathVariable Integer id, @RequestBody MolecularProtocol entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setMolecularProtocolID(id);
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
