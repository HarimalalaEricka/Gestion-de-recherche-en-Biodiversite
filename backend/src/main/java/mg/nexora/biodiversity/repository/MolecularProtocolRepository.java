package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.MolecularProtocol;

public interface MolecularProtocolRepository extends JpaRepository<MolecularProtocol, Integer> {
}
