package mg.nexora.biodiversity.controller;

import java.util.Map;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import mg.nexora.biodiversity.dto.*;
import mg.nexora.biodiversity.repository.UtilisateurRepository;
import mg.nexora.biodiversity.model.*;
import mg.nexora.biodiversity.model.keys.AgentRechercheKey;
import mg.nexora.biodiversity.service.AgentRechercheService;

@RestController
@RequestMapping("/api/agent-recherches")
public class AgentRechercheController {
    private final AgentRechercheService service;
    private final UtilisateurRepository utilisateurRepository;

    public AgentRechercheController(AgentRechercheService service,
                                    UtilisateurRepository utilisateurRepository) {
        this.service = service;
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping
    public Page<AgentRecherche> findAll( @RequestParam Map<String, String> params,Pageable pageable) {
        return service.findAll(params,pageable);
    }

    @GetMapping("/{agentIDId}/{rechercheIDId}")
    public ResponseEntity<AgentRecherche> findById(@PathVariable Integer agentIDId, @PathVariable Integer rechercheIDId) {
        AgentRechercheKey id = new AgentRechercheKey();
        id.setAgent(agentIDId);
        id.setRecherche(rechercheIDId);
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AgentRecherche> create(@RequestBody AgentRecherche entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @DeleteMapping("/{agentIDId}/{rechercheIDId}")
    public ResponseEntity<Void> delete(@PathVariable Integer agentIDId, @PathVariable Integer rechercheIDId) {
        AgentRechercheKey id = new AgentRechercheKey();
        id.setAgent(agentIDId);
        id.setRecherche(rechercheIDId);
        if (service.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /** Authentifié : POST /api/agent-recherches/recherches/{rechercheId}/collaborateurs */
    @PostMapping("/recherches/{rechercheId}/collaborateurs")
    public ResponseEntity<Map<String, String>> inviter(@PathVariable Integer rechercheId,
                                                       @Valid @RequestBody CollaboratorRequest req,
                                                       Authentication auth) {
        Utilisateur invitant = utilisateurRepository.findByEmail(auth.getName())   // adapte selon ton JWT
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        service.inviter(rechercheId, req.email(), req.role(), invitant);
        return ResponseEntity.accepted().body(Map.of("message", "Invitation envoyée à " + req.email()));
    }

    @PostMapping("/accepter")
    public ResponseEntity<Void> accepter(
            @Valid @RequestBody CollaboratorAcceptRequest req,
            Authentication authentication) {

        service.accepter(
                req.token(),
                req.rechercheId(),
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}
