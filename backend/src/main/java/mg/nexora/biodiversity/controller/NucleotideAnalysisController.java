package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.NucleotideAnalysis;
import mg.nexora.biodiversity.service.NucleotideAnalysisService;

@RestController
@RequestMapping("/api/nucleotide-analysiss")
public class NucleotideAnalysisController {
    private final NucleotideAnalysisService service;

    public NucleotideAnalysisController(NucleotideAnalysisService service) {
        this.service = service;
    }

    @GetMapping
    public List<NucleotideAnalysis> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NucleotideAnalysis> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NucleotideAnalysis> create(@RequestBody NucleotideAnalysis entity) {
        NucleotideAnalysis saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NucleotideAnalysis> update(@PathVariable Integer id, @RequestBody NucleotideAnalysis entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setNucleotideAnalysisID(id);
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
