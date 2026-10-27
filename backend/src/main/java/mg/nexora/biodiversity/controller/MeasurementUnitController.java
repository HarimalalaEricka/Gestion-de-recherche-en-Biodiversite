package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.MeasurementUnit;
import mg.nexora.biodiversity.service.MeasurementUnitService;

@RestController
@RequestMapping("/api/measurement-units")
public class MeasurementUnitController {
    private final MeasurementUnitService service;

    public MeasurementUnitController(MeasurementUnitService service) {
        this.service = service;
    }

    @GetMapping
    public List<MeasurementUnit> findAll() {
        return service.findAll();
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
