package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.TableauPersonnalise;

public interface TableauPersonnaliseRepository extends JpaRepository<TableauPersonnalise, Integer> {
}
