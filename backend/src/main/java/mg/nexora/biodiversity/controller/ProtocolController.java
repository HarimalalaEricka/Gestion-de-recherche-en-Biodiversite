package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.Protocol;
import mg.nexora.biodiversity.service.ProtocolService;

@RestController
@RequestMapping("/api/protocols")
public class ProtocolController {
    private final ProtocolService service;

    public ProtocolController(ProtocolService service) {
        this.service = service;
    }

    @GetMapping
    public List<Protocol> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Protocol> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Protocol> create(@RequestBody Protocol entity) {
        Protocol saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Protocol> update(@PathVariable Integer id, @RequestBody Protocol entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setProtocolID(id);
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
