package mg.nexora.biodiversity.repository;

import mg.nexora.biodiversity.model.Token;
import mg.nexora.biodiversity.model.enums.TypeToken;
import mg.nexora.biodiversity.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenHashAndTypeToken(String tokenHash, TypeToken typeToken);
    void deleteByUtilisateurAndTypeToken(Utilisateur utilisateur, TypeToken typeToken);
}