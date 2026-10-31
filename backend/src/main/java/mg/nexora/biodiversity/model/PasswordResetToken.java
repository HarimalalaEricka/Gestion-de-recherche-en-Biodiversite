package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_hash", nullable = false, unique = true)
    private String tokenHash;

    @ManyToOne(optional = false)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Column(name = "expire_le", nullable = false)
    private LocalDateTime expireLe;

    private boolean utilise = false;

    public Long getId() { return id; }
    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    public LocalDateTime getExpireLe() { return expireLe; }
    public void setExpireLe(LocalDateTime expireLe) { this.expireLe = expireLe; }
    public boolean isUtilise() { return utilise; }
    public void setUtilise(boolean utilise) { this.utilise = utilise; }
}