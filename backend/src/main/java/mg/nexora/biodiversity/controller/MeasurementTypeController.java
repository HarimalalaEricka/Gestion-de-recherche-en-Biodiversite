package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.MeasurementType;
import mg.nexora.biodiversity.service.MeasurementTypeService;

@RestController
@RequestMapping("/api/measurement-types")
public class MeasurementTypeController {
    private final MeasurementTypeService service;

    public MeasurementTypeController(MeasurementTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<MeasurementType> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeasurementType> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MeasurementType> create(@RequestBody MeasurementType entity) {
        MeasurementType saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeasurementType> update(@PathVariable Integer id, @RequestBody MeasurementType entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setMeasurementTypeID(id);
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
