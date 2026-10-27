package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.NucleotideAnalysis;

public interface NucleotideAnalysisRepository extends JpaRepository<NucleotideAnalysis, Integer> {
}
