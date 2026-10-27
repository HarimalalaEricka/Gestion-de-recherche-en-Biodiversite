package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "notificationid")

    private Integer notificationID;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)

    private TypeNotification type;

    @Column(name = "titre", nullable = false)

    private String titre;

    @Column(name = "lue", nullable = false)

    private Boolean lue;

    @Column(name = "created_at", nullable = false)

    private LocalDateTime createdAt;

    @Column(name = "read_at")

    private LocalDateTime readAt;

    @Column(name = "message")

    private String message;

    @Column(name = "lien")

    private String lien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateurid", nullable = false)
    private Utilisateur utilisateurID;

    public Integer getNotificationID() { return notificationID; }

    public void setNotificationID(Integer notificationID) { this.notificationID = notificationID; }

    public TypeNotification getType() { return type; }

    public void setType(TypeNotification type) { this.type = type; }

    public String getTitre() { return titre; }

    public void setTitre(String titre) { this.titre = titre; }

    public Boolean getLue() { return lue; }

    public void setLue(Boolean lue) { this.lue = lue; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getReadAt() { return readAt; }

    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getLien() { return lien; }

    public void setLien(String lien) { this.lien = lien; }

    public Utilisateur getUtilisateurID() { return utilisateurID; }

    public void setUtilisateurID(Utilisateur utilisateurID) { this.utilisateurID = utilisateurID; }

}
