package mg.nexora.biodiversity.controller;

import java.util.List;
import java.util.Map;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.*;
import mg.nexora.biodiversity.model.enums.*;
import mg.nexora.biodiversity.service.*;
import mg.nexora.biodiversity.dto.CreerRechercheRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/recherches")
public class RechercheController {
    private final RechercheService service;
    private final UtilisateurService utilisateurService;

    public RechercheController(RechercheService service, UtilisateurService utilisateurService) {
        this.service = service;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public Page<Recherche> findAll(@RequestParam Map<String, String> params, @Param("page") Pageable pageable) {
        return service.findAll(params, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recherche> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Recherche> create(@RequestBody Recherche entity) {
        Recherche saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recherche> update(@PathVariable Integer id, @RequestBody Recherche entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setRechercheID(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Recherche> partialUpdate(@PathVariable Integer id, @RequestBody Recherche entity) {
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

    @PostMapping("/creer")
    public Recherche creer(@RequestBody CreerRechercheRequest req, Authentication auth) {
        return service.creerAvecAgent(req, auth.getName());
    }

    @GetMapping("/mes-recherches")
    public Page<Recherche> mesRecherches(
            Authentication authentication,
            @RequestParam(required = false) Confidentiality confidentialite,
            Pageable pageable) {

        System.out.println("CTRL confidentialite = " + confidentialite);
        Integer utilisateurId = utilisateurService.findIdByEmail(authentication.getName());
        return service.findByUtilisateur(utilisateurId, confidentialite, pageable);
    }
}
