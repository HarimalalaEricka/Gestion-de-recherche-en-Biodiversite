package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "media_recherche")
public class MediaRecherche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "media_rechercheid")

    private Integer mediaRechercheID;

    @Column(name = "accessuri")

    private String accessURI;

    @Column(name = "caption")

    private String caption;

    @Column(name = "comments")

    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateurid", nullable = false)
    private Utilisateur utilisateurID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rechercheid", nullable = false)
    private Recherche rechercheID;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_media", nullable = false)

    private TypeMedia typeMedia;

    public Integer getMediaRechercheID() { return mediaRechercheID; }

    public void setMediaRechercheID(Integer mediaRechercheID) { this.mediaRechercheID = mediaRechercheID; }

    public String getAccessURI() { return accessURI; }

    public void setAccessURI(String accessURI) { this.accessURI = accessURI; }

    public String getCaption() { return caption; }

    public void setCaption(String caption) { this.caption = caption; }

    public String getComments() { return comments; }

    public void setComments(String comments) { this.comments = comments; }

    public Utilisateur getUtilisateurID() { return utilisateurID; }

    public void setUtilisateurID(Utilisateur utilisateurID) { this.utilisateurID = utilisateurID; }

    public Recherche getRechercheID() { return rechercheID; }

    public void setRechercheID(Recherche rechercheID) { this.rechercheID = rechercheID; }

    public TypeMedia getTypeMedia() { return typeMedia; }

    public void setTypeMedia(TypeMedia typeMedia) { this.typeMedia = typeMedia; }

}
