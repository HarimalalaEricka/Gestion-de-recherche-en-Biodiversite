package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.OccurrenceAgent;
import mg.nexora.biodiversity.model.keys.OccurrenceAgentKey;
import mg.nexora.biodiversity.service.OccurrenceAgentService;

@RestController
@RequestMapping("/api/occurrence-agents")
public class OccurrenceAgentController {
    private final OccurrenceAgentService service;

    public OccurrenceAgentController(OccurrenceAgentService service) {
        this.service = service;
    }

    @GetMapping
    public List<OccurrenceAgent> findAll() {
        return service.findAll();
    }

    @GetMapping("/{agentIDId}/{occurrenceIDId}")
    public ResponseEntity<OccurrenceAgent> findById(@PathVariable Integer agentIDId, @PathVariable Integer occurrenceIDId) {
        OccurrenceAgentKey id = new OccurrenceAgentKey();
        id.setAgentID(agentIDId);
        id.setOccurrenceID(occurrenceIDId);
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OccurrenceAgent> create(@RequestBody OccurrenceAgent entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @DeleteMapping("/{agentIDId}/{occurrenceIDId}")
    public ResponseEntity<Void> delete(@PathVariable Integer agentIDId, @PathVariable Integer occurrenceIDId) {
        OccurrenceAgentKey id = new OccurrenceAgentKey();
        id.setAgentID(agentIDId);
        id.setOccurrenceID(occurrenceIDId);
        if (service.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
