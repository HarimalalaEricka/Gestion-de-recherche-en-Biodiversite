package mg.nexora.biodiversity.service;

import mg.nexora.biodiversity.model.PasswordResetToken;
import mg.nexora.biodiversity.model.Utilisateur;
import mg.nexora.biodiversity.model.enums.StatutUser;
import mg.nexora.biodiversity.repository.PasswordResetTokenRepository;
import mg.nexora.biodiversity.repository.UtilisateurRepository;
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
public class PasswordResetService {

    private final UtilisateurRepository utilisateurRepo;
    private final PasswordResetTokenRepository tokenRepo;
    private final PasswordEncoder encoder;
    private final EmailService emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.reset-token-minutes}")
    private long validiteMinutes;

    private final SecureRandom random = new SecureRandom();

    public PasswordResetService(UtilisateurRepository utilisateurRepo,
                                PasswordResetTokenRepository tokenRepo,
                                PasswordEncoder encoder,
                                EmailService emailService) {
        this.utilisateurRepo = utilisateurRepo;
        this.tokenRepo = tokenRepo;
        this.encoder = encoder;
        this.emailService = emailService;
    }

    /** Étape 1 : crée le jeton et envoie le lien. Ne dit jamais si l'e-mail existe. */
    @Transactional
    public void demander(String email) {
        Utilisateur u = utilisateurRepo.findByEmail(email).orElse(null);
        if (u == null || u.getStatut() != StatutUser.ACTIVE) {
            return;   // silence volontaire
        }

        tokenRepo.deleteByUtilisateur(u);   // un seul lien valide à la fois

        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        PasswordResetToken prt = new PasswordResetToken();
        prt.setTokenHash(hash(token));
        prt.setUtilisateur(u);
        prt.setExpireLe(LocalDateTime.now().plusMinutes(validiteMinutes));
        tokenRepo.save(prt);

        String lien = frontendUrl + "/auth/reinitialisation?token=" + token;
        emailService.envoyerLienReinitialisation(u.getEmail(), lien);
    }

    /** Étape 2 : vérifie le jeton et change le mot de passe. Renvoie false si le jeton est invalide. */
    @Transactional
    public boolean reinitialiser(String token, String nouveauMotDePasse) {
        PasswordResetToken prt = tokenRepo.findByTokenHash(hash(token)).orElse(null);

        if (prt == null || prt.isUtilise() || prt.getExpireLe().isBefore(LocalDateTime.now())) {
            return false;
        }

        Utilisateur u = prt.getUtilisateur();
        u.setMotDePasse(encoder.encode(nouveauMotDePasse));
        utilisateurRepo.save(u);

        prt.setUtilise(true);
        tokenRepo.save(prt);
        return true;
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