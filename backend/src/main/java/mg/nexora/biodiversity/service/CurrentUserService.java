package mg.nexora.biodiversity.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import mg.nexora.biodiversity.model.Utilisateur;
import mg.nexora.biodiversity.repository.UtilisateurRepository;

@Service
public class CurrentUserService {

    private final UtilisateurRepository utilisateurs;

    public CurrentUserService(UtilisateurRepository utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    private Authentication auth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return auth;
    }

    /** ID de l'utilisateur connecté, retrouvé depuis l'e-mail du JWT. */
    public Integer id() {
        return utilisateurs.findByEmail(auth().getName())
                .map(Utilisateur::getUtilisateurID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    public boolean isAdmin() {
        return auth().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}