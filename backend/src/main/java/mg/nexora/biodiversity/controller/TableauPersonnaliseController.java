package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.TableauPersonnalise;
import mg.nexora.biodiversity.service.TableauPersonnaliseService;

@RestController
@RequestMapping("/api/tableau-personnalises")
public class TableauPersonnaliseController {
    private final TableauPersonnaliseService service;

    public TableauPersonnaliseController(TableauPersonnaliseService service) {
        this.service = service;
    }

    @GetMapping
    public List<TableauPersonnalise> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableauPersonnalise> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TableauPersonnalise> create(@RequestBody TableauPersonnalise entity) {
        TableauPersonnalise saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableauPersonnalise> update(@PathVariable Integer id, @RequestBody TableauPersonnalise entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setTableauPersonnaliseID(id);
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
