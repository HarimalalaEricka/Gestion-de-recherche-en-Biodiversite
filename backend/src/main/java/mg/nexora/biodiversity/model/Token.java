package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import mg.nexora.biodiversity.model.enums.TypeToken;

@Entity
@Table(name = "token")
public class Token {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "type_token", nullable = false)
    private TypeToken typeToken;

    public Long getId() { return id; }
    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    public LocalDateTime getExpireLe() { return expireLe; }
    public void setExpireLe(LocalDateTime expireLe) { this.expireLe = expireLe; }
    public boolean isUtilise() { return utilise; }
    public void setUtilise(boolean utilise) { this.utilise = utilise; }
    public TypeToken getTypeToken() { return typeToken; }
    public void setTypeToken(TypeToken typeToken) { this.typeToken = typeToken; }
}