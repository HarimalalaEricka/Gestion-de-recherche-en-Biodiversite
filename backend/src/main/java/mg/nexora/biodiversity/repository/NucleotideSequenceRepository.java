package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.NucleotideSequence;

public interface NucleotideSequenceRepository extends JpaRepository<NucleotideSequence, Integer> {
}
