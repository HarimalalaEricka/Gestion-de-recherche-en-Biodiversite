package mg.nexora.biodiversity.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import mg.nexora.biodiversity.model.MediaRecherche;
import mg.nexora.biodiversity.model.enums.*;
import mg.nexora.biodiversity.service.*;

@RestController
@RequestMapping("/api/medias-recherche")
public class MediaRechercheController {
    private final MediaRechercheService service;
    private final UtilisateurService utilisateurService;

    public MediaRechercheController(MediaRechercheService service, UtilisateurService utilisateurService) {
        this.service = service;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaRecherche> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MediaRecherche> create(@RequestBody MediaRecherche entity) {
        MediaRecherche saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediaRecherche> update(@PathVariable Integer id, @RequestBody MediaRecherche entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setMediaRechercheID(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MediaRecherche> partialUpdate(@PathVariable Integer id, @RequestBody MediaRecherche entity) {
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

    @GetMapping
    public Page<MediaRecherche> findAll(@RequestParam Map<String, String> params, Pageable pageable) {
        return service.findAll(params, pageable);
    }

    @GetMapping("/mes-medias")
    public Page<MediaRecherche> mesMedias(
            Authentication authentication,
            @RequestParam(required = false) Integer rechercheID,
            @RequestParam(required = false) TypeMedia typeMedia,   
            Pageable pageable) {

        Integer utilisateurId = utilisateurService.findIdByEmail(authentication.getName());
        return service.findAllMesMedias(utilisateurId, rechercheID, typeMedia, pageable);
    }

    @GetMapping("/mes-medias/{rechercheID}")
    public Page<MediaRecherche> mesMediasDeLaRecherche(
            @PathVariable Integer rechercheID,
            Authentication authentication,
            Pageable pageable) {

        Integer utilisateurId = utilisateurService.findIdByEmail(authentication.getName());
        return service.findMesMedias(rechercheID, utilisateurId, pageable);
    }
}
