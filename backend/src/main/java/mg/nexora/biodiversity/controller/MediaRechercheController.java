package mg.nexora.biodiversity.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import mg.nexora.biodiversity.model.MediaRecherche;
import mg.nexora.biodiversity.model.enums.*;
import mg.nexora.biodiversity.service.MediaRechercheService;
import mg.nexora.biodiversity.service.UtilisateurService;

@RestController
@RequestMapping("/api/medias-recherche")
public class MediaRechercheController {

    private final MediaRechercheService service;
    private final UtilisateurService utilisateurService;

    public MediaRechercheController(MediaRechercheService service, UtilisateurService utilisateurService) {
        this.service = service;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public Page<MediaRecherche> findAll(@RequestParam Map<String, String> params, Pageable pageable) {
        return service.findAll(params, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaRecherche> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Tous mes médias, filtres optionnels : recherche, type, texte
    @GetMapping("/mes-medias")
    public Page<MediaRecherche> mesMedias(
            Authentication authentication,
            @RequestParam(required = false) Integer rechercheID,
            @RequestParam(required = false) TypeMedia typeMedia,
            @RequestParam(required = false) String q,
            Pageable pageable) {

        return service.findAllMesMedias( rechercheID, typeMedia, q, pageable);
    }

    // Mes médias d'une recherche (même logique, l'ID est dans le chemin)
    @GetMapping("/mes-medias/{rechercheID}")
    public Page<MediaRecherche> mesMediasDeLaRecherche(
            @PathVariable Integer rechercheID,
            @RequestParam(required = false) TypeMedia typeMedia,
            @RequestParam(required = false) String q,
            Authentication authentication,
            Pageable pageable) {

        return service.findAllMesMedias( rechercheID, typeMedia, q, pageable);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaRecherche> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("rechercheID") Integer rechercheID,
            @RequestParam(required = false) String caption,
            @RequestParam(required = false) String comments,
            @RequestParam(required = true) TypeMediaRecherche type,
            Authentication authentication) {

        Integer utilisateurId = utilisateurService.findIdByEmail(authentication.getName());
        MediaRecherche saved = service.upload(file, rechercheID, caption, comments, utilisateurId, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
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
}