package mg.nexora.biodiversity.controller;

import jakarta.validation.Valid;
import mg.nexora.biodiversity.dto.LoginRequest;
import mg.nexora.biodiversity.dto.LoginResponse;
import mg.nexora.biodiversity.model.Utilisateur;
import mg.nexora.biodiversity.repository.UtilisateurRepository;
import mg.nexora.biodiversity.security.JwtService;
import mg.nexora.biodiversity.model.enums.StatutUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UtilisateurRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthController(UtilisateurRepository repo, PasswordEncoder encoder, JwtService jwtService) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        Utilisateur u = repo.findByEmail(req.email()).orElse(null);

        if (u == null || u.getStatut() != StatutUser.ACTIVE || u.getMotDePasse() == null
                || !encoder.matches(req.motDePasse(), u.getMotDePasse())) {
            return ResponseEntity.status(401).body(Map.of("erreur", "Identifiants incorrects"));
        }

        String role = u.getRole().name();
        String token = jwtService.generate(u.getEmail(), role);
        return ResponseEntity.ok(new LoginResponse(token, u.getEmail(), u.getNom(), role));
    }

    /** Renvoie l'utilisateur connecté (le frontend s'en sert pour vérifier le jeton). */
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        Utilisateur u = repo.findByEmail(auth.getName()).orElse(null);
        if (u == null) {
            return ResponseEntity.status(401).body(Map.of("erreur", "Utilisateur introuvable"));
        }
        return ResponseEntity.ok(Map.of(
                "email", u.getEmail(),
                "nom", u.getNom() == null ? "" : u.getNom(),
                "role", u.getRole().name()));
    }
}