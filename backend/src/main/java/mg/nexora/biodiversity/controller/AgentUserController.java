package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.AgentUser;
import mg.nexora.biodiversity.model.keys.AgentUserKey;
import mg.nexora.biodiversity.service.AgentUserService;

@RestController
@RequestMapping("/api/agent-users")
public class AgentUserController {
    private final AgentUserService service;

    public AgentUserController(AgentUserService service) {
        this.service = service;
    }

    @GetMapping
    public List<AgentUser> findAll() {
        return service.findAll();
    }

    @GetMapping("/{agentIDId}/{utilisateurIDId}")
    public ResponseEntity<AgentUser> findById(@PathVariable Integer agentIDId, @PathVariable Integer utilisateurIDId) {
        AgentUserKey id = new AgentUserKey();
        id.setAgentID(agentIDId);
        id.setUtilisateurID(utilisateurIDId);
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AgentUser> create(@RequestBody AgentUser entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @DeleteMapping("/{agentIDId}/{utilisateurIDId}")
    public ResponseEntity<Void> delete(@PathVariable Integer agentIDId, @PathVariable Integer utilisateurIDId) {
        AgentUserKey id = new AgentUserKey();
        id.setAgentID(agentIDId);
        id.setUtilisateurID(utilisateurIDId);
        if (service.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
