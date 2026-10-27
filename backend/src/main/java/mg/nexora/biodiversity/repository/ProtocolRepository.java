package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Protocol;

public interface ProtocolRepository extends JpaRepository<Protocol, Integer> {
}
