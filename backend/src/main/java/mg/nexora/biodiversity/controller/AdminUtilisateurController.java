package mg.nexora.biodiversity.controller;

import jakarta.validation.Valid;
import mg.nexora.biodiversity.dto.InvitationRequest;
import mg.nexora.biodiversity.model.Utilisateur;
import mg.nexora.biodiversity.model.enums.Role;
import mg.nexora.biodiversity.model.enums.StatutUser;
import mg.nexora.biodiversity.model.enums.TypeToken;
import mg.nexora.biodiversity.repository.UtilisateurRepository;
import mg.nexora.biodiversity.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/utilisateurs")
public class AdminUtilisateurController {

    private final UtilisateurRepository repo;
    private final TokenService tokenService;

    public AdminUtilisateurController(UtilisateurRepository repo, TokenService tokenService) {
        this.repo = repo;
        this.tokenService = tokenService;
    }

    @PostMapping("/invitation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> inviter(@Valid @RequestBody InvitationRequest req) {
        if (repo.findByEmail(req.email()).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("erreur", "Cette adresse est déjà utilisée"));
        }

        Utilisateur u = new Utilisateur();
        u.setNom(req.nom());
        u.setEmail(req.email());
        u.setRole(req.role());                 // enum Role
        u.setStatut(StatutUser.INVITE);        // pas de mot de passe pour l'instant
        u.setDateCreation(java.time.LocalDateTime.now());
        repo.save(u);

        tokenService.demander(u.getEmail(), TypeToken.INVITATION);
        return ResponseEntity.ok(Map.of("message", "Invitation envoyée"));
    }

    /** Renvoie un lien si l'ancien a expiré (le précédent est supprimé automatiquement). */
    @PostMapping("/{id}/renvoyer-invitation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> renvoyer(@PathVariable Integer id) {
        Utilisateur u = repo.findById(id).orElse(null);
        if (u == null || u.getStatut() != StatutUser.INVITE) {
            return ResponseEntity.badRequest().body(Map.of("erreur", "Aucune invitation en attente"));
        }
        tokenService.demander(u.getEmail(), TypeToken.INVITATION);
        return ResponseEntity.ok(Map.of("message", "Invitation renvoyée"));
    }
}