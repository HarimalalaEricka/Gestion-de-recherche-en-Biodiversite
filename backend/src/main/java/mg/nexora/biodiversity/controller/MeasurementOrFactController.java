package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.MeasurementOrFact;
import mg.nexora.biodiversity.service.MeasurementOrFactService;

@RestController
@RequestMapping("/api/measurement-or-facts")
public class MeasurementOrFactController {
    private final MeasurementOrFactService service;

    public MeasurementOrFactController(MeasurementOrFactService service) {
        this.service = service;
    }

    @GetMapping
    public List<MeasurementOrFact> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeasurementOrFact> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MeasurementOrFact> create(@RequestBody MeasurementOrFact entity) {
        MeasurementOrFact saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeasurementOrFact> update(@PathVariable Integer id, @RequestBody MeasurementOrFact entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setMeasurementOrFactID(id);
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
