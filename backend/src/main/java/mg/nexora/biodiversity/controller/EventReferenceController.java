package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.EventReference;
import mg.nexora.biodiversity.model.keys.EventReferenceKey;
import mg.nexora.biodiversity.service.EventReferenceService;

@RestController
@RequestMapping("/api/event-references")
public class EventReferenceController {
    private final EventReferenceService service;

    public EventReferenceController(EventReferenceService service) {
        this.service = service;
    }

    @GetMapping
    public List<EventReference> findAll() {
        return service.findAll();
    }

    @GetMapping("/{eventIDId}/{referenceIDId}")
    public ResponseEntity<EventReference> findById(@PathVariable Integer eventIDId, @PathVariable Integer referenceIDId) {
        EventReferenceKey id = new EventReferenceKey();
        id.setEventID(eventIDId);
        id.setReferenceID(referenceIDId);
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EventReference> create(@RequestBody EventReference entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @DeleteMapping("/{eventIDId}/{referenceIDId}")
    public ResponseEntity<Void> delete(@PathVariable Integer eventIDId, @PathVariable Integer referenceIDId) {
        EventReferenceKey id = new EventReferenceKey();
        id.setEventID(eventIDId);
        id.setReferenceID(referenceIDId);
        if (service.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
