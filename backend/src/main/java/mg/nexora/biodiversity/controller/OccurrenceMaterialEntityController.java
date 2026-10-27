package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.OccurrenceMaterialEntity;
import mg.nexora.biodiversity.model.keys.OccurrenceMaterialEntityKey;
import mg.nexora.biodiversity.service.OccurrenceMaterialEntityService;

@RestController
@RequestMapping("/api/occurrence-material-entitys")
public class OccurrenceMaterialEntityController {
    private final OccurrenceMaterialEntityService service;

    public OccurrenceMaterialEntityController(OccurrenceMaterialEntityService service) {
        this.service = service;
    }

    @GetMapping
    public List<OccurrenceMaterialEntity> findAll() {
        return service.findAll();
    }

    @GetMapping("/{occurrenceIDId}/{materialEntityIDId}")
    public ResponseEntity<OccurrenceMaterialEntity> findById(@PathVariable Integer occurrenceIDId, @PathVariable Integer materialEntityIDId) {
        OccurrenceMaterialEntityKey id = new OccurrenceMaterialEntityKey();
        id.setOccurrenceID(occurrenceIDId);
        id.setMaterialEntityID(materialEntityIDId);
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OccurrenceMaterialEntity> create(@RequestBody OccurrenceMaterialEntity entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @DeleteMapping("/{occurrenceIDId}/{materialEntityIDId}")
    public ResponseEntity<Void> delete(@PathVariable Integer occurrenceIDId, @PathVariable Integer materialEntityIDId) {
        OccurrenceMaterialEntityKey id = new OccurrenceMaterialEntityKey();
        id.setOccurrenceID(occurrenceIDId);
        id.setMaterialEntityID(materialEntityIDId);
        if (service.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
