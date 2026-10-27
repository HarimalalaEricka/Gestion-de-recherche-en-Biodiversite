package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.MediaRecherche;
import mg.nexora.biodiversity.service.MediaRechercheService;

@RestController
@RequestMapping("/api/medias-recherche")
public class MediaRechercheController {
    private final MediaRechercheService service;

    public MediaRechercheController(MediaRechercheService service) {
        this.service = service;
    }

    @GetMapping
    public List<MediaRecherche> findAll() {
        return service.findAll();
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
