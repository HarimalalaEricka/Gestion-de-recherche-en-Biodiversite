package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.AgentRecherche;
import mg.nexora.biodiversity.model.keys.AgentRechercheKey;
import mg.nexora.biodiversity.service.AgentRechercheService;

@RestController
@RequestMapping("/api/agent-recherches")
public class AgentRechercheController {
    private final AgentRechercheService service;

    public AgentRechercheController(AgentRechercheService service) {
        this.service = service;
    }

    @GetMapping
    public List<AgentRecherche> findAll() {
        return service.findAll();
    }

    @GetMapping("/{agentIDId}/{rechercheIDId}")
    public ResponseEntity<AgentRecherche> findById(@PathVariable Integer agentIDId, @PathVariable Integer rechercheIDId) {
        AgentRechercheKey id = new AgentRechercheKey();
        id.setAgentID(agentIDId);
        id.setRechercheID(rechercheIDId);
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AgentRecherche> create(@RequestBody AgentRecherche entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @DeleteMapping("/{agentIDId}/{rechercheIDId}")
    public ResponseEntity<Void> delete(@PathVariable Integer agentIDId, @PathVariable Integer rechercheIDId) {
        AgentRechercheKey id = new AgentRechercheKey();
        id.setAgentID(agentIDId);
        id.setRechercheID(rechercheIDId);
        if (service.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
