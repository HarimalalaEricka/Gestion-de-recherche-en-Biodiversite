package mg.nexora.biodiversity.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.MeasurementUnit;
import mg.nexora.biodiversity.service.MeasurementUnitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/measurement-units")
public class MeasurementUnitController {
    private final MeasurementUnitService service;

    public MeasurementUnitController(MeasurementUnitService service) {
        this.service = service;
    }

    @GetMapping
    public Page<MeasurementUnit> findAll(@RequestParam Map<String, String> params, Pageable pageable) {
        return service.findAll(params, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeasurementUnit> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MeasurementUnit> create(@RequestBody MeasurementUnit entity) {
        MeasurementUnit saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeasurementUnit> update(@PathVariable Integer id, @RequestBody MeasurementUnit entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setMeasurementUnitID(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MeasurementUnit> partialUpdate(@PathVariable Integer id, @RequestBody MeasurementUnit entity) {
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
