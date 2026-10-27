package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.BibliographicResource;

public interface BibliographicResourceRepository extends JpaRepository<BibliographicResource, Integer> {
}
