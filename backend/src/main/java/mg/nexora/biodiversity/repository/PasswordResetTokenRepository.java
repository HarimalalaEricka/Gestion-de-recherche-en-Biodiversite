package mg.nexora.biodiversity.repository;

import mg.nexora.biodiversity.model.PasswordResetToken;
import mg.nexora.biodiversity.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByTokenHash(String tokenHash);
    void deleteByUtilisateur(Utilisateur utilisateur);
}