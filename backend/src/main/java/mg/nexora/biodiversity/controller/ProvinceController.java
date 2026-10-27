package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.Province;
import mg.nexora.biodiversity.service.ProvinceService;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {
    private final ProvinceService service;

    public ProvinceController(ProvinceService service) {
        this.service = service;
    }

    @GetMapping
    public List<Province> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Province> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Province> create(@RequestBody Province entity) {
        Province saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Province> update(@PathVariable Integer id, @RequestBody Province entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setProvinceID(id);
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
