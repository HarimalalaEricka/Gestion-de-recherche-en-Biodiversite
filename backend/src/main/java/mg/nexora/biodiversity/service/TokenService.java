package mg.nexora.biodiversity.service;

import mg.nexora.biodiversity.model.Token;
import mg.nexora.biodiversity.model.Utilisateur;
import mg.nexora.biodiversity.model.enums.StatutUser;
import mg.nexora.biodiversity.model.enums.TypeToken;
import mg.nexora.biodiversity.repository.TokenRepository;
import mg.nexora.biodiversity.repository.UtilisateurRepository;
import mg.nexora.biodiversity.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class TokenService {

    private final UtilisateurRepository utilisateurRepo;
    private final TokenRepository tokenRepo;
    private final PasswordEncoder encoder;
    private final EmailService emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.reset-token-minutes}")
    private long resetMinutes;

    @Value("${app.invitation-token-days}")
    private long invitationJours;

    private final SecureRandom random = new SecureRandom();

    public TokenService(UtilisateurRepository utilisateurRepo,
                        TokenRepository tokenRepo,
                        PasswordEncoder encoder,
                        EmailService emailService) {
        this.utilisateurRepo = utilisateurRepo;
        this.tokenRepo = tokenRepo;
        this.encoder = encoder;
        this.emailService = emailService;
    }

    /**
     * Crée un jeton du type demandé et envoie le lien par e-mail.
     * Ne dit jamais si l'adresse existe : en cas d'échec, il ne se passe rien.
     */
    @Transactional
    public void demander(String email, TypeToken type) {
        Utilisateur u = utilisateurRepo.findByEmail(email).orElse(null);

        // Adresse inconnue, ou statut qui ne correspond pas au type demandé
        if (u == null || u.getStatut() != statutRequis(type)) {
            return;
        }

        tokenRepo.deleteByUtilisateurAndTypeToken(u, type);   // un seul lien valide par type

        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        Token t = new Token();
        t.setTokenHash(hash(token));
        t.setUtilisateur(u);
        t.setTypeToken(type);
        t.setExpireLe(expiration(type));
        tokenRepo.save(t);

        String lien = frontendUrl + cheminFrontend(type) + "?token=" + token;
        emailService.envoyerLien(u.getEmail(), lien, type);
    }

    /**
     * Vérifie le jeton et enregistre le mot de passe.
     * Pour une invitation, le compte passe aussi de INVITE à ACTIVE.
     * Renvoie false si le jeton est invalide, expiré ou déjà utilisé.
     */
    @Transactional
    public boolean valider(String token, String motDePasse, TypeToken type) {
        Token t = tokenRepo.findByTokenHashAndTypeToken(hash(token), type).orElse(null);

        if (t == null || t.isUtilise() || t.getExpireLe().isBefore(LocalDateTime.now())) {
            return false;
        }

        Utilisateur u = t.getUtilisateur();
        if (u.getStatut() != statutRequis(type)) {
            return false;
        }

        u.setMotDePasse(encoder.encode(motDePasse));
        u.setStatut(StatutUser.ACTIVE);   // sans effet pour un reset (déjà ACTIVE)
        utilisateurRepo.save(u);

        t.setUtilise(true);
        tokenRepo.save(t);
        return true;
    }

    // ---------- Ce qui change selon le type ----------

    private StatutUser statutRequis(TypeToken type) {
        return switch (type) {
            case RESET_PASSWORD -> StatutUser.ACTIVE;
            case INVITATION -> StatutUser.INVITE;
        };
    }

    private LocalDateTime expiration(TypeToken type) {
        return switch (type) {
            case RESET_PASSWORD -> LocalDateTime.now().plusMinutes(resetMinutes);
            case INVITATION -> LocalDateTime.now().plusDays(invitationJours);
        };
    }

    private String cheminFrontend(TypeToken type) {
        return switch (type) {
            case RESET_PASSWORD -> "/auth/reinitialisation";
            case INVITATION -> "/auth/activation-du-compte";
        };
    }

    private String hash(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}