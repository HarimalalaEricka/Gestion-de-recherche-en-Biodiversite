package mg.nexora.biodiversity.model;

import jakarta.persistence.*;
import java.time.*;
import java.math.BigDecimal;
import mg.nexora.biodiversity.model.enums.*;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "utilisateurid")

    private Integer utilisateurID;

    @Column(name = "nom", nullable = false)

    private String nom;

    @Column(name = "prenom")

    private String prenom;

    @Column(name = "telephone")

    private String telephone;

    @Column(name = "email", nullable = false)

    private String email;

    @Column(name = "mot_de_passe", nullable = false)

    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)

    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)

    private StatutUser statut;

    @Column(name = "date_creation", nullable = false)

    private LocalDateTime dateCreation;

    public Integer getUtilisateurID() { return utilisateurID; }

    public void setUtilisateurID(Integer utilisateurID) { this.utilisateurID = utilisateurID; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }

    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }

    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }

    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public StatutUser getStatut() { return statut; }

    public void setStatut(StatutUser statut) { this.statut = statut; }

    public LocalDateTime getDateCreation() { return dateCreation; }

    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

}
