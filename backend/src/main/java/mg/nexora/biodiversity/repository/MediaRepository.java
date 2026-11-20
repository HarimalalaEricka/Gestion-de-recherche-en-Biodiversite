package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import mg.nexora.biodiversity.model.Media;

public interface MediaRepository extends JpaRepository<Media, Integer>, JpaSpecificationExecutor<Media> {
}
