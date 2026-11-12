package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import mg.nexora.biodiversity.model.Utilisateur;

import java.util.Optional;

public interface UtilisateurRepository
        extends JpaRepository<Utilisateur, Integer>, JpaSpecificationExecutor<Utilisateur> {
    Optional<Utilisateur> findByEmail(String email);
}