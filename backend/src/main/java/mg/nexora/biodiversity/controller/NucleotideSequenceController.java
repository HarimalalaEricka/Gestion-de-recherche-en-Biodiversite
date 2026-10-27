package mg.nexora.biodiversity.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mg.nexora.biodiversity.model.NucleotideSequence;
import mg.nexora.biodiversity.service.NucleotideSequenceService;

@RestController
@RequestMapping("/api/nucleotide-sequences")
public class NucleotideSequenceController {
    private final NucleotideSequenceService service;

    public NucleotideSequenceController(NucleotideSequenceService service) {
        this.service = service;
    }

    @GetMapping
    public List<NucleotideSequence> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NucleotideSequence> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NucleotideSequence> create(@RequestBody NucleotideSequence entity) {
        NucleotideSequence saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NucleotideSequence> update(@PathVariable Integer id, @RequestBody NucleotideSequence entity) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Force the URL identifier to be the identifier used for the update.
        entity.setNucleotideSequenceID(id);
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
