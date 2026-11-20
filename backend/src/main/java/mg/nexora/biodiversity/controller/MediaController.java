package mg.nexora.biodiversity.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.*;
import mg.nexora.biodiversity.service.*;
import mg.nexora.biodiversity.model.enums.*;
import mg.nexora.biodiversity.service.MediaService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/medias")
public class MediaController {
    private final MediaService service;
    private final UtilisateurService utilisateurService;

    public MediaController(MediaService service, UtilisateurService utilisateurService) {
        this.service = service;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public Page<Media> findAll(@RequestParam Map<String, String> params, Pageable pageable) {
        return service.findAll(params, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Media> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Media> create(@RequestBody Media entity) {
        Media saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Media> update(@PathVariable Integer id, @RequestBody Media entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setMediaID(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Media> partialUpdate(@PathVariable Integer id, @RequestBody Media entity) {
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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Media> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("occurrenceID") Integer occurrenceID,
            @RequestParam(required = false) String caption,
            @RequestParam(required = false) String comments,
            @RequestParam(required = false) String captureDevice,
            @RequestParam(required = false) String associatedObservationReference,
            Authentication authentication) {

        Integer utilisateurId = utilisateurService.findIdByEmail(authentication.getName());
        Media saved = service.upload(file, occurrenceID, caption, comments,
                captureDevice, associatedObservationReference, utilisateurId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
