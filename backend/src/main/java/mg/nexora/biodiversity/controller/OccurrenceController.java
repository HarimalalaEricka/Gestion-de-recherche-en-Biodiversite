package mg.nexora.biodiversity.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.*;
import mg.nexora.biodiversity.service.OccurrenceService;
import mg.nexora.biodiversity.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/occurrences")
public class OccurrenceController {
    private final OccurrenceService service;
    private final UtilisateurRepository utilisateurRepository;

    public OccurrenceController(OccurrenceService service, UtilisateurRepository utilisateurRepository) {
        this.service = service;
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping
     public Page<Occurrence> findAll(@RequestParam Map<String, String> params, Pageable pageable) {
        return service.findAll(params, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Occurrence> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Occurrence> create(
            @RequestBody Occurrence entity,
            Authentication authentication) {

        Utilisateur utilisateur = utilisateurRepository
            .findByEmail(authentication.getName())
            .orElseThrow(() -> new RuntimeException(
                "Utilisateur introuvable."
            ));

        Occurrence saved = service.save(entity, utilisateur.getUtilisateurID());

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Occurrence> update(
            @PathVariable Integer id,
            @RequestBody Occurrence entity) {

        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        entity.setOccurrenceID(id);

        return ResponseEntity.ok(service.update(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Occurrence> partialUpdate(@PathVariable Integer id, @RequestBody Occurrence entity) {
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
